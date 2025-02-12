package com.example;

import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;

public class LambdaInvoker {
	public void Lambda() {
		//      JButton button = new JButton("通知を送信");
		//      button.addActionListener(new ActionListener() {
		//          @Override
		//          public void actionPerformed(ActionEvent e) {
		// Lambdaクライアントの作成
		System.out.println("aaaaaaaaaaaaaaaaaaaaa");
		AWSLambdaClient lambdaClient = (AWSLambdaClient) AWSLambdaClientBuilder.defaultClient();

		// Lambda関数へのリクエストを作成
		InvokeRequest request = new InvokeRequest()
				.withFunctionName("test") // Lambda関数の名前
				.withPayload("{\"message\": \"ボタンが押されました\"}");

		try {
			InvokeResult result = lambdaClient.invoke(request);
			System.out.println("Lambda function invoked: " + result);
		} catch (Exception ex) {
			// エラー処理
			ex.printStackTrace();
		}
		//          }
		//      });
		// ... (他のUI部品の設定)
	}

	//  public static void main(String[] args) {
	//  	System.out.println("test");
	//      new LambdaInvoker().setVisible(true);
	//  }
}
