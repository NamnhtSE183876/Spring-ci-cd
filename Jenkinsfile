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
    }

    stages {
        stage('Build with maven') {
            steps {
                // Build dự án Spring Boot bằng Maven
                sh 'mvn --version'
                sh 'java --version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Packaging/Pushing image') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t lagux/springboot .'
                    sh 'docker push lagux/springboot'
                }
            }
        }

        stage('Deploy SQL server') {
            steps {
                echo 'Deploying SQL Server and cleaning'
                sh 'docker pull mcr.microsoft.com/mssql/server:2019-latest'
                sh 'docker network create dev || echo "this network already exists"'
                sh 'docker run -d --name mssql --network dev \\
                    -e ACCEPT_EULA=Y \\
                    -e SA_PASSWORD=$DB_PASSWORD \\
                    -p 1433:1433 mcr.microsoft.com/mssql/server:2019-latest'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy Docker container cho ứng dụng Spring Boot
                sh """
                docker stop your-app-container || true
                docker rm your-app-container || true
                docker run -d --name your-app-container \\
                    --network dev \\
                    -e SPRING_DATASOURCE_URL=$DB_URL \\
                    -e SPRING_DATASOURCE_USERNAME=$DB_USERNAME \\
                    -e SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \\
                    -e SPRING_MAIL_USERNAME=$MAIL_USERNAME \\
                    -e SPRING_MAIL_PASSWORD=$MAIL_PASSWORD \\
                    -e GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID \\
                    -p 8080:8080 lagux/springboot
                """
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