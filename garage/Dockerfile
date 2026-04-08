# Vi lånar bara binärfilen från garage-imagen
FROM dxflrs/garage:v2.2.0 AS garage-bin

# Vi använder alpine som bas för att faktiskt kunna köra ett skript
FROM alpine:latest

# Installera de bibliotek som garage-binären behöver (ofta krävs libc/libgcc)
RUN apk add --no-cache libc6-compat gcompat

# Kopiera in garage-binären
COPY --from=garage-bin /garage /usr/local/bin/garage

# Kopiera in skriptet
COPY init-garage.sh /usr/local/bin/init-garage.sh

# Tvätta bort Windows-radbrytningar (viktigt!) och gör körbar
RUN sed -i 's/\r$//' /usr/local/bin/init-garage.sh && \
    chmod +x /usr/local/bin/init-garage.sh

ENTRYPOINT ["/bin/sh", "/usr/local/bin/init-garage.sh"]
