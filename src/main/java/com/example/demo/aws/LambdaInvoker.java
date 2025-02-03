package com.example.demo.aws;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaInvoker {
    public static void main(String[] args) {
        AWSLambda lambda = AWSLambdaClientBuilder.defaultClient();

        // Lambda関数のARN
        String functionArn = "arn:aws:lambda:us-east-1:211125403670:function:task-test";

        // 呼び出すLambda関数に渡すペイロード
        String payload = "{\"message\": \"Hello from Eclipse\"}";

        InvokeRequest request = new InvokeRequest()
                .withFunctionName(functionArn)
                .withPayload(payload);

        try {
            InvokeResult result = lambda.invoke(request);
            // 結果の処理 (ログ出力など)
            System.out.println("Result: " + result.getPayload().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}