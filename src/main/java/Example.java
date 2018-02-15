import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Example {

    @RequestMapping("/")
    String home() {
        
        String target = System.getenv("TARGET");
        if (target == null) {
            target = "localhost";
        }

        Settings settings = Settings.builder()
        .put("client.transport.sniff", false).build();

        TransportClient client = new PreBuiltTransportClient(settings);
        try {
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(target), 9300));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        XContentBuilder builder;
        try {
            builder = XContentFactory.jsonBuilder()
            .startObject()
            .field("fullName", "Test")
            .field("dateOfBirth", new Date())
            .field("age", "10")
            .endObject();

            IndexResponse response = client.prepareIndex("people", "Doe")
            .setSource(builder).get();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
   

    System.out.println("about to close");
    client.close();
        
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Example.class, args);
    }

}
