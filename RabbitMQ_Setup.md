# RabbitMQ Setup Guide for Windows

This guide will help you set up RabbitMQ on a Windows 11 system, integrate it with your Spring Boot application, and test its functionality.

## Step 1: Install Erlang
1. Visit the [Erlang Downloads page](https://www.erlang.org/downloads).
2. Download the appropriate Windows installer.
3. Run the installer and follow the instructions.
4. Add the Erlang `bin` directory to your system's `PATH` environment variable.

## Step 2: Install RabbitMQ
1. Visit the [RabbitMQ Downloads page](https://www.rabbitmq.com/install-windows.html).
2. Download the RabbitMQ installer for Windows.
3. Run the installer and follow the instructions.

## Step 3: Start RabbitMQ

### Option 1: Run as a Service
1. Open `Command Prompt` or `PowerShell` as Administrator.
2. Install RabbitMQ as a Windows service:
   ```bash
   rabbitmq-service install
   rabbitmq-service start
   ```

### Option 2: Run Manually
1. Navigate to the RabbitMQ `sbin` directory, typically:
   ```
   C:\Program Files\RabbitMQ Server\rabbitmq_server-<version>\sbin
   ```
2. Start the server:
   ```bash
   rabbitmq-server
   ```

## Step 4: Enable RabbitMQ Management Plugin
Enable the RabbitMQ Management Plugin to access the web interface:
```bash
rabbitmq-plugins enable rabbitmq_management
```
Access the management interface at:
```
http://localhost:15672
```
Default credentials:
- **Username**: `guest`
- **Password**: `guest`

## Step 5: Configure RabbitMQ in Spring Boot
Add the following dependency to your `build.gradle`:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-amqp'
}
```

Configure RabbitMQ connection in `application.properties`:
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

## Step 6: Define RabbitMQ Queue
Create a configuration class to define the queue:
```java
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue rabbitCareQueue() {
        return new Queue("rabbitCareQueue", true);
    }
}
```

## Step 7: Listen to Messages
Create a service to listen to messages:
```java
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListenerService {

    @RabbitListener(queues = "rabbitCareQueue")
    public void receiveLocationMessage(LocationDTO locationDTO) {
        System.out.println("Received location: " + locationDTO.getName());
        // Process the received location
    }
}
```

## Step 8: Test RabbitMQ
### Create the Queue
1. Go to the RabbitMQ Management Interface.
2. Navigate to the **Queues** tab.
3. Add a new queue with the name `rabbitCareQueue`.

### Send a Test Message
Send a message using RabbitMQ CLI:
```bash
rabbitmqadmin publish exchange=amq.default routing_key=rabbitCareQueue payload='{"name":"Vet Clinic","latitude":45.2671,"longitude":19.8335}'
```

### Verify the Message
Check the Spring Boot application logs for the received message.

## Step 9: Integrate with Your Application
Update your map or UI logic to include the received locations for display.

---

For further assistance or debugging, refer to the [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html).
