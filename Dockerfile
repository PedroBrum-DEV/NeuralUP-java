# Usar Java 21
FROM eclipse-temurin:21-jdk-alpine

# Diretório da aplicação
WORKDIR /app

# Copiar tudo para o container
COPY . ./

# Dar permissão ao Maven wrapper
RUN chmod +x mvnw

# Build do projeto (gera o fast-jar)
RUN ./mvnw clean package -DskipTests -Dquarkus.package.type=fast-jar

# Entrar na pasta gerada pelo Quarkus
WORKDIR /app/target/quarkus-app

# Comando de execução correto
CMD ["java", "-jar", "quarkus-run.jar"]
