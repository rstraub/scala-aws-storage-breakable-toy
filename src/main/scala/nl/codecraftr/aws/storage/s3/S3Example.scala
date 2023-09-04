package nl.codecraftr.aws.storage.s3

import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.Bucket

import java.util

object S3Example {
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

    val listBucketsResponse = s3client.listBuckets

    // Display the bucket names
    val buckets: util.List[Bucket] = listBucketsResponse.buckets
    println("Buckets:")
    buckets.forEach(bucket => println(bucket.name()))
  }
}
