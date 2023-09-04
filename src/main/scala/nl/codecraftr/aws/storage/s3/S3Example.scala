package nl.codecraftr.aws.storage.s3

import software.amazon.awssdk.auth.credentials.{
  AwsBasicCredentials,
  StaticCredentialsProvider
}
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{
  Bucket,
  DeleteObjectRequest,
  PutObjectRequest
}

import java.util

object S3Example {
  private val bucketKey = "cats-1.json"

  def main(args: Array[String]): Unit = {
    val accessKey = args(0)
    val secretKey = args(1)
    println(s"access key: $accessKey")
    println(s"secret key: $secretKey")

    val credentials =
      AwsBasicCredentials.create(accessKey, secretKey)

    val s3client = S3Client
      .builder()
      .region(Region.EU_WEST_1)
      .credentialsProvider(StaticCredentialsProvider.create(credentials))
      .build()

    listS3Buckets(s3client)

    deleteCat(s3client)
    saveCat(s3client)

    s3client.close()
  }

  private def deleteCat(client: S3Client) = {
    client.deleteObject(
      DeleteObjectRequest.builder
        .bucket("aws-scala-bucket")
        .key(bucketKey)
        .build()
    )
  }

  private def saveCat(s3client: S3Client) = {
    val request =
      PutObjectRequest.builder
        .bucket("aws-scala-bucket")
        .key(bucketKey)
        .contentType("application/json")
        .build

    val json =
      """
          |{
          |    "name": "Garfield",
          |    "age": 38,
          |    "colour": "ginger and black"
          |}
          |""".stripMargin

    s3client.putObject(request, RequestBody.fromString(json))
  }

  private def listS3Buckets(s3client: S3Client): Unit = {
    val listBucketsResponse = s3client.listBuckets

    // Display the bucket names
    val buckets: util.List[Bucket] = listBucketsResponse.buckets
    println("Buckets:")
    buckets.forEach(bucket => println(bucket.name()))
  }
}
