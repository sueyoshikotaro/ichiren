//package com.example;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//
//import com.amazonaws.services.lambda.AWSLambdaClient;
//import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
//import com.amazonaws.services.lambda.model.InvokeRequest;
//import com.amazonaws.services.lambda.model.InvokeResult;
//
//public class LambdaInvoker extends JFrame {
//    public LambdaInvoker() {
//        JButton button = new JButton("通知を送信");
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Lambdaクライアントの作成
//                AWSLambdaClient lambdaClient = AWSLambdaClientBuilder.defaultClient();
//
//                // Lambda関数へのリクエストを作成
//                InvokeRequest request = new InvokeRequest()
//                        .withFunctionName("your_function_name") // Lambda関数の名前
//                        .withPayload("{\"message\": \"ボタンが押されました\"}");
//
//                try {
//                    InvokeResult result = lambdaClient.invoke(request);
//                    System.out.println("Lambda function invoked: " + result);
//                } catch (Exception ex) {
//                    // エラー処理
//                    ex.printStackTrace();
//                }
//            }
//        });
//        // ... (他のUI部品の設定)
//    }
//
//    public static void main(String[] args) {
//        new LambdaInvoker().setVisible(true);
//    }
//}