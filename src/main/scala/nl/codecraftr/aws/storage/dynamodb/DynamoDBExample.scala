package nl.codecraftr.aws.storage.dynamodb

import software.amazon.awssdk.auth.credentials.{
  DefaultCredentialsProvider,
  StaticCredentialsProvider
}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{
  AttributeValue,
  GetItemRequest
}

import java.net.URI
import scala.jdk.CollectionConverters.MapHasAsJava

object DynamoDBExample extends App {
  val awsRegion = Region.of("local")
  val credentialsProvider = DefaultCredentialsProvider.create()

  val dynamoDbClient: DynamoDbClient = DynamoDbClient
    .builder()
    .endpointOverride(new URI("http://localhost:8000"))
    .region(awsRegion)
    .credentialsProvider(
      StaticCredentialsProvider.create(
        credentialsProvider.resolveCredentials()
      )
    )
    .build()

  val getRequest = GetItemRequest
    .builder()
    .tableName("Music")
    .key(
      Map(
        "Artist" -> AttributeValue.builder().s("No One You Know").build(),
        "SongTitle" -> AttributeValue.builder().s("Call Me Today").build()
      ).asJava
    )
    .build()

  val result = dynamoDbClient.getItem(getRequest)
  println("Result: " + result.item())
}
