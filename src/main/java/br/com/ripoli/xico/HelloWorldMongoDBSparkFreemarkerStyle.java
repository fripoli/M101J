package br.com.ripoli.xico;

import com.mongodb.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.net.UnknownHostException;

public class HelloWorldMongoDBSparkFreemarkerStyle {
	public static void main(String[] args) throws UnknownHostException {
		final Configuration configuration = new Configuration();

		configuration.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");

		MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		DB database = client.getDB("test");

		final DBCollection collection = database.getCollection("names");

		Spark.get(new Route("/") {
			          @Override
			          public Object handle(Request request, Response response) {
				          Template helloTemplate = null;
				          StringWriter writer = new StringWriter();
				          try {
					          helloTemplate = configuration.getTemplate("hello.ftl");

					          DBObject document = collection.findOne();

					          helloTemplate.process(document, writer);
				          } catch (Exception e) {
					          halt(500);
					          e.printStackTrace();
				          }

				          return writer;
			          }
		          }

		);
	}

}
