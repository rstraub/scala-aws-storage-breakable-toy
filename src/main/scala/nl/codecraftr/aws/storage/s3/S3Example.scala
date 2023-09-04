package nl.codecraftr.aws.storage.s3

import nl.codecraftr.aws.storage.model.Cat
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
  GetObjectRequest,
  PutObjectRequest
}
import io.circe.generic.auto._
import io.circe.jawn.decode
import io.circe.syntax._
import io.circe.syntax._
import nl.codecraftr.aws.storage.model.Cat.garfield

import java.util

object S3Example {
  private val bucketKey = "cats-1.json"
  private val bucket = "aws-scala-bucket"

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
    retrieveCat(s3client)

    s3client.close()
  }

  private def listS3Buckets(s3Client: S3Client): Unit = {
    val listBucketsResponse = s3Client.listBuckets

    // Display the bucket names
    val buckets: util.List[Bucket] = listBucketsResponse.buckets
    println("Buckets:")
    buckets.forEach(bucket => println(bucket.name()))
  }

  private def deleteCat(s3Client: S3Client) = {
    s3Client.deleteObject(
      DeleteObjectRequest.builder
        .bucket(bucket)
        .key(bucketKey)
        .build()
    )
  }

  private def saveCat(s3Client: S3Client) = {
    val request =
      PutObjectRequest.builder
        .bucket(bucket)
        .key(bucketKey)
        .contentType("application/json")
        .build

    val json =
      garfield.asJson.toString()

    s3Client.putObject(request, RequestBody.fromString(json))
  }

  private def retrieveCat(s3Client: S3Client): Unit = {
    val request =
      GetObjectRequest.builder().bucket(bucket).key(bucketKey).build()

    val response = s3Client.getObjectAsBytes(request).asByteArray()

    decode[Cat](new String(response)) match {
      case Left(e)    => println(s"oops, something went wrong: $e")
      case Right(cat) => println(s"response: $cat")
    }
  }
}
