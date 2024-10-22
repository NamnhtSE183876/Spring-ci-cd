pipeline {
    agent any

    tools {
        maven 'maven'
    }
    environment {
        // Cấu hình các biến môi trường cần thiết
        DB_URL = credentials('db-url')
        DB_USERNAME = credentials('db-username')
        DB_PASSWORD = credentials('db-password')
        MAIL_USERNAME = credentials('mail-username')
        MAIL_PASSWORD = credentials('mail-password')
        GOOGLE_CLIENT_ID = credentials('google-api-key')
        FIREBASE_FILE = credentials('firebase-file')
        HOST = credentials("host-ip")
    }

    stages {
        stage('Build with Maven') {
            steps {
                // Kiểm tra phiên bản Maven và Java
                sh 'mvn --version'
                sh 'java --version'
                // Build dự án Spring Boot
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Packaging/Pushing Image') {
            steps {
                // Đăng nhập vào Docker Registry và đẩy image
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t lagux/springboot .'
                    sh 'docker push lagux/springboot'
                }
            }
        }

//         stage('Deploy SQL Server') {
//             steps {
//                 echo 'Deploying SQL Server and cleaning'
//                 // Kéo image SQL Server
//                 sh 'docker pull mcr.microsoft.com/mssql/server:2019-latest'
//                 // Tạo mạng Docker nếu chưa tồn tại
//                 sh 'docker network create dev || echo "This network already exists"'
//                 // Dừng và xóa container SQL Server nếu đã tồn tại
//                 sh 'docker stop mssql || true'
//                 sh 'docker rm mssql || true'
//                 // Chạy container SQL Server mới
//                 sh """
//                 docker run -d --name mssql --network dev \
//                 -e ACCEPT_EULA=Y \
//                 -e SA_PASSWORD=$DB_PASSWORD \
//                 -p 1433:1433 \
//                 mcr.microsoft.com/mssql/server:2019-latest
//                 """
//             }
//         }

        stage('Deploy Redis') {
                    steps {
                        echo 'Deploying Redis...'

                        sh 'docker pull redis:latest'

                        sh 'docker network create dev || echo "This network already exists"'

                        sh 'docker stop redis || true'
                        sh 'docker rm redis || true'

                        sh """
                        docker run -d --name redis --network dev \
                            -p 6379:6379 \
                            redis:latest
                        """
                    }
                }

        stage('Deploy Application') {
            steps {
                echo "Deploying application with the following environment variables:"
                echo "DB_URL: $DB_URL"
                echo "DB_USERNAME: $DB_USERNAME"
                echo "DB_PASSWORD: $DB_PASSWORD"
                echo "MAIL_USERNAME: $MAIL_USERNAME"
                echo "MAIL_PASSWORD: $MAIL_PASSWORD"
                echo "GOOGLE_CLIENT_ID: $GOOGLE_CLIENT_ID"

                // Sử dụng withCredentials để lấy file bí mật
                withCredentials([file(credentialsId: 'firebase-file', variable: 'FIREBASE_FILE_PATH')]) {
                    // Deploy Docker container cho ứng dụng Spring Boot
                    sh 'docker stop myapp || true'
                    sh 'docker rm myapp || true'
                    sh """
                    docker run -d --name myapp --network dev \
                        -e SPRING_DATASOURCE_URL="jdbc:sqlserver://${HOST}:1433;databaseName=Koi_project;encrypt=true;trustServerCertificate=true" \
                        -e VNPAY_URL="http://${HOST}/api/pay/vn-pay-callback" \
                        -e SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_REDIRECT_URI="http://${HOST}/authenticate/login-google" \
                        -e SPRING_DATASOURCE_USERNAME="$DB_USERNAME" \
                        -e SPRING_DATASOURCE_PASSWORD="$DB_PASSWORD" \
                        -e SPRING_MAIL_USERNAME="$MAIL_USERNAME" \
                        -e SPRING_MAIL_PASSWORD="$MAIL_PASSWORD" \
                        -e GOOGLE_CLIENT_ID="$GOOGLE_CLIENT_ID" \
                        -e FIREBASE_FILE="/app/config/firebase.json" \
                        --mount type=bind,source="${FIREBASE_FILE_PATH}",target=/app/config/firebase.json \
                        -p 8082:8080 lagux/springboot
                    """
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker sau khi pipeline chạy xong
            sh 'docker system prune -f'
        }

        success {
            echo 'Pipeline hoàn thành thành công!'
        }

        failure {
            echo 'Pipeline thất bại.'
        }
    }
}
