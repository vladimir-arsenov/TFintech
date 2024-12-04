| **configType**                           | **Mode**                          | **Cnt** | **Score** | **Error**       | **Units** |
|------------------------------------------|-----------------------------------|---------|-----------|-----------------|-----------|
| KafkaBenchmarkWithConfigurations.test    | simple                            | thrpt   | 10        | 0,807 ± 0,266   | ops/ms    |
| KafkaBenchmarkWithConfigurations.test    | load_balancing                    | thrpt   | 10        | 0,493 ± 0,168   | ops/ms    |
| KafkaBenchmarkWithConfigurations.test    | multiple_consumers                | thrpt   | 9         | 1,705 ± 0,630   | ops/ms    |
| KafkaBenchmarkWithConfigurations.test    | load_balancing_multiple_consumers | thrpt   | 10        | 0,611 ± 0,544   | ops/ms    |
| KafkaBenchmarkWithConfigurations.test    | stress_test                       | thrpt   | 10        | 0,115 ± 0,152   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.test | simple                            | thrpt   | 10        | 0,028 ± 0,004   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.test | load_balancing                    | thrpt   | 10        | 0,023 ± 0,004   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.test | multiple_consumers                | thrpt   | 10        | 0,025 ± 0,002   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.test | load_balancing_multiple_consumers | thrpt   | 10        | 0,018 ± 0,003   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.test | stress_test                       | thrpt   | 10        | 0,007 ± 0,001   | ops/ms    |
| RabbitMQBenchmarkWithConfigurations.tes  | simple                            | avgt    | 10        | 10,231 ± 0,827  | ms/op     |
| RabbitMQBenchmarkWithConfigurations.test | load_balancing                    | avgt    | 10        | 18,800 ± 0,385  | ms/op     |
| RabbitMQBenchmarkWithConfigurations.test | multiple_consumers                | avgt    | 10        | 19,740 ± 0,644  | ms/op     |
| RabbitMQBenchmarkWithConfigurations.test | load_balancing_multiple_consumers | avgt    | 10        | 23,518 ± 2,755  | ms/op     |
| RabbitMQBenchmarkWithConfigurations.test | stress_test                       | avgt    | 10        | 40,331 ± 6,549  | ms/op     |
| KafkaBenchmarkWithConfigurations.test    | simple                            | avgt    | 10        | 32,206 ± 9,632  | ms/op     |
| KafkaBenchmarkWithConfigurations.test    | load_balancing                    | avgt    | 10        | 48,939 ± 7,318  | ms/op     |
| KafkaBenchmarkWithConfigurations.test    | multiple_consumers                | avgt    | 10        | 49,787 ± 9,211  | ms/op     |
| KafkaBenchmarkWithConfigurations.test    | load_balancing_multiple_consumers | avgt    | 10        | 61,701 ± 9,914  | ms/op     |
| KafkaBenchmarkWithConfigurations.test    | stress_test                       | avgt    | 10        | 69,264  ± 9,447 | ms/op     |

### Вывод:
- **Kafka** подходит для сценариев с высокой нагрузкой и большим количеством продюсеров и консюмеров, где критичны высокая пропускная способность и масштабируемость. 
- Хорошо справляется с большой нагрузкой (stress test).
- Latency может быть, выше чем у Rabbit из-за механизмов подтверждения и записи в log.
- **RabbitMQ** лучше использовать при работе с небольшими сообщениями, где его архитектура более эффективна.
- Пропускная способность ниже, чем у Kafka, особенно в стресс-тестах.
- Latency может увеличиваться при нагрузке, особенно при включении подтверждений сообщений (ack)