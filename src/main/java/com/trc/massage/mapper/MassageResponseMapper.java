package com.trc.massage.mapper;

import com.trc.massage.binding.CancellationPolicy;
import com.trc.massage.binding.Massage;
import com.trc.massage.binding.Price;
import com.trc.massage.binding.Response;
import com.trc.massage.model.MassageService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MassageResponseMapper {

    private static final String AVAILABLE_STATUS = "AVAILABLE";
    private static final Comparator<CancellationPolicy> CANCELLATION_POLICY_COMPARATOR = Comparator.comparing(CancellationPolicy::getDate).thenComparing(Comparator.comparingDouble(CancellationPolicy::getAmount).reversed());
    private static final DateTimeFormatter DATE_MESSAGE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final String CANCELLATION_POLICIES_WITHOUT_PENALTY_UNTIL = "Sin gastos de cancelación hasta el %s.";
    private static final String CANCELLATION_POLICIES_BETWEEN = "Entre el %s y el %s: %s.";
    private static final String CANCELLATION_POLICIES_NOT_REFUNDABLE_FROM = "Desde el %s: no reembolsable.";


    public static List<MassageService> buildMassages(Response response, LocalDate requestDate) {
        return response.getMassages().stream()
                .filter(p -> AVAILABLE_STATUS.equals(p.getStatus()))
                .map(m -> buildMassage(m, requestDate))
                .toList();
    }

    private static MassageService buildMassage(Massage massage, LocalDate requestDate) {
        var code = massage.getCode();
        var name = massage.getName();
        var price = massage.getPrice();
        var duration = massage.getDuration();
        var policies = buildPolicies(massage.getCancellationPolicies(), price, requestDate);
        return new MassageService(code, name, duration, price, policies);
    }

    public static List<String> buildPolicies(List<CancellationPolicy> cancellationPolicies, Price totalPriceMassage, LocalDate requestDate) {
        var validCancellationPoliciesSorted = getValidCancellationPoliciesSorted(cancellationPolicies, requestDate);
        var cancellationPolicyRanges = buildPoliciesRange(validCancellationPoliciesSorted, totalPriceMassage, requestDate);
        return buildCancellationPoliciesMessages(cancellationPolicyRanges);
    }

    private static List<CancellationPolicy> getValidCancellationPoliciesSorted(List<CancellationPolicy> cancellationPolicies, LocalDate requestDate) {
        var cancellationPoliciesSortedByDateAndAmount = cancellationPolicies.stream()
                .sorted(CANCELLATION_POLICY_COMPARATOR)
                .collect(Collectors.toList());
        return cancellationPoliciesSortedByDateAndAmount.stream()
                .filter(policy -> isValidPolicy(cancellationPoliciesSortedByDateAndAmount.indexOf(policy), cancellationPoliciesSortedByDateAndAmount, requestDate))
                .collect(Collectors.toList());
    }

    public static List<CancellationPolicyRange> buildPoliciesRange(List<CancellationPolicy> validCancellationPoliciesSorted, Price priceMassage, LocalDate requestDate) {
        if (validCancellationPoliciesSorted.isEmpty()) {
            return List.of(new CancellationPolicyRange(requestDate, requestDate, priceMassage));
        }
        var result = new ArrayList<CancellationPolicyRange>();
        if (hasInitialRefundableRange(validCancellationPoliciesSorted)) {
            var first = validCancellationPoliciesSorted.getFirst();
            result.add(new CancellationPolicyRange(LocalDate.now(), first.getDate().minusDays(1), new Price(0.0, first.getPrice().getCurrency())));
        }
        validCancellationPoliciesSorted.stream()
                .map(policy -> buildCancellationPolicyRange(validCancellationPoliciesSorted, requestDate, policy))
                .forEach(result::add);
        if (hasLastIsPartiallyRefundable(priceMassage, result)) {
            result.add(new CancellationPolicyRange(requestDate, requestDate, priceMassage));
        }
        return result;
    }

    private static CancellationPolicyRange buildCancellationPolicyRange(List<CancellationPolicy> validCancellationPolicies, LocalDate requestDate, CancellationPolicy policy) {
        return new CancellationPolicyRange(
                getStartDate(policy),
                getEndDate(requestDate, validCancellationPolicies.indexOf(policy), validCancellationPolicies),
                policy.getPrice());
    }

    private static List<String> buildCancellationPoliciesMessages(List<CancellationPolicyRange> cancellationPolicyRanges) {
        return IntStream.range(0, cancellationPolicyRanges.size())
                .mapToObj(i -> buildMessagePolicies(i, cancellationPolicyRanges))
                .toList();
    }

    private static String buildMessagePolicies(int position, List<CancellationPolicyRange> cancellationPolicies) {
        var policyRange = cancellationPolicies.get(position);
        if (isFirst(position) && isRefundable(policyRange)) {
            return CANCELLATION_POLICIES_WITHOUT_PENALTY_UNTIL.formatted(policyRange.endDate().format(DATE_MESSAGE_FORMATTER));
        }
        if (isLast(position, cancellationPolicies.size())) {
            return CANCELLATION_POLICIES_NOT_REFUNDABLE_FROM.formatted(policyRange.startDate().format(DATE_MESSAGE_FORMATTER));
        }
        return CANCELLATION_POLICIES_BETWEEN.formatted(policyRange.startDate().format(DATE_MESSAGE_FORMATTER), policyRange.endDate().format(DATE_MESSAGE_FORMATTER), policyRange.price());
    }

    private static boolean isRefundable(CancellationPolicyRange cancellationPolicyRange) {
        return cancellationPolicyRange.price().getAmount() == 0f;
    }

    private static boolean isFirst(int position) {
        return position == 0;
    }

    private static boolean isLast(int position, int size) {
        return position == size - 1;
    }

    private static boolean hasLastIsPartiallyRefundable(Price totalPriceMassage, List<CancellationPolicyRange> result) {
        return !result.getLast().price().getAmount().equals(totalPriceMassage.getAmount());
    }

    private static boolean hasInitialRefundableRange(List<CancellationPolicy> validCancellationPolicies) {
        CancellationPolicy first = validCancellationPolicies.getFirst();
        return first.getDate().isAfter(LocalDate.now()) && first.getAmount() > 0;
    }

    private static LocalDate getStartDate(CancellationPolicy policy) {
        if (isEqualsOrBeforeDate(policy.getDate(), LocalDate.now())) {
            return LocalDate.now();
        }
        return policy.getDate();
    }

    private static LocalDate getEndDate(LocalDate requestDate, int position, List<CancellationPolicy> cancellationPoliciesSorted) {
        if (isLast(position, cancellationPoliciesSorted.size())) {
            return requestDate.minusDays(1);
        } else {
            return cancellationPoliciesSorted.get(position + 1).getDate().minusDays(1);
        }
    }

    private static boolean isValidPolicy(int position, List<CancellationPolicy> cancellationPoliciesSorted, LocalDate requestDate) {
        if (isFirst(position)) {
            return true;
        }
        var cancellationPolicy = cancellationPoliciesSorted.get(position);
        if (isEqualsOrBeforeDate(requestDate, cancellationPolicy.getDate())) {
            return false;
        }
        return cancellationPoliciesSorted.get(position - 1).getAmount() < cancellationPolicy.getAmount();
    }

    private static boolean isEqualsOrBeforeDate(LocalDate requestDate, LocalDate date) {
        return requestDate.isEqual(date) || requestDate.isBefore(date); // Estas me dan igual ya que siempre será 100%
    }
}
