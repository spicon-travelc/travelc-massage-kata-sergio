# Políticas de cancelación en el Salón de Masajes

## Descripción
Un salón de masajes ofrece servicios de masaje y está preparando su plataforma para ofrecerlos en línea. Cada masaje tiene un precio, un estado de disponibilidad, una duración específica y una política de cancelación.

## Preparación
Descarga y ejecuta localmente el servicio que devuelve la disponibilidad de masajes. Para ello, sigue las instrucciones en: [Instrucciones del Servicio](https://github.com/spicon-travelc/travelc-massage-kata-service)

## Servicio de disponibilidad
Hay un servicio que nos proporciona todos los masajes disponibles para un día específico. Cada masaje tiene la siguiente información:

- Código del masaje
- Nombre
- Estado
- Duración
- Precio
- Política de cancelación

Para acceder al servicio: `GET - http://localhost:38080/massages/quote/2024-10-02`

## Objetivo
Se necesita un servicio que devuelva los masajes disponibles para un día específico, su precio, duración y que muestre las políticas de cancelación en un formato comprensible para el cliente.

Cosas a tener en cuenta:

- Las políticas de cancelación se expresan en un mensaje (ver ejemplo).
- El día del masaje siempre será 100 % no reembolsable.
- En los mensajes, no puede haber fechas anteriores al día actual.


Ejemplo del mensaje:

- Sin gastos de cancelación hasta el 22 ene 2024.
- Entre el 23 ene 2024 y el 24 ene 2024: 100 €.
- Desde el 25 ene 2024: no reembolsable.
