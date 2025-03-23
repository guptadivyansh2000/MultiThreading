import java.util.Random;

public class ProducerConsumerProblem {
    private static final int PRODUCER_TOTAL_CAPACITY = 300;
    private static final int PRODUCER_LOWER_THRESHOLD = 200;
    private static final int NUM_CONSUMERS = 5;
    private static final int DELAY_MS = 500;

    // Shared Stock Management
    private static class SharedStock {
        private int stockInHand;

        private final Random random = new Random();

        public SharedStock(int initialStock) {
            this.stockInHand = initialStock;
        }

        public synchronized void produce() {

            if (stockInHand < PRODUCER_LOWER_THRESHOLD) {
                int produceAmount = random.nextInt(100, 300);
                if (stockInHand + produceAmount <= PRODUCER_TOTAL_CAPACITY) {
                    stockInHand += produceAmount;
                    System.out.printf("Producer Thread: PTC: %d, SiH: %d PLT: %d%n",
                            PRODUCER_TOTAL_CAPACITY, stockInHand,PRODUCER_LOWER_THRESHOLD);
                }
            }
        }

        public synchronized int consume(int consumerIndex) {
            // parameters of consumer
            int consumerTotalCapacity = 300;
            int consumerLowerThreshold = 100;
            int consumerCurrentStock = random.nextInt(50, 200);

            // print the consumer initial state
            System.out.printf("Consumer-%d Thread CTC: %d, CSIH: %d, CLT: %d%n",
                    consumerIndex, consumerTotalCapacity,
                    consumerCurrentStock, consumerLowerThreshold);

            // Request amount
            int requestAmount = random.nextInt(100, 300);
            System.out.printf("Consumer-%d Thread: Requesting %d units from Producer%n",
                    consumerIndex, requestAmount);

            // Check if request can be fulfilled
            if (consumerCurrentStock + requestAmount <= consumerTotalCapacity
                    && stockInHand >= requestAmount) {

                // Supply stock
                stockInHand -= requestAmount;

                // print the details of producer supply to consumer
                System.out.printf("Producer Supplying %d Units to Consumer-%d Thread. PTC: %d, SiH: %d, LT: %d%n",
                        requestAmount, consumerIndex,
                        PRODUCER_TOTAL_CAPACITY, stockInHand,
                        PRODUCER_LOWER_THRESHOLD);

                // Print final consumer state
                System.out.println();
                System.out.printf("CTC: %d, CSIH: %d, CLT: %d%n",
                        consumerTotalCapacity,
                        consumerCurrentStock + requestAmount,
                        consumerLowerThreshold);

                return requestAmount;
            }
            return 0;
        }
        public synchronized void printCurrentState(){

            System.out.printf("Producer Thread: PTC: %d, SiH: %d PLT: %d %n",
                    PRODUCER_TOTAL_CAPACITY, stockInHand,PRODUCER_LOWER_THRESHOLD);

        }
    }

    // Producer Thread
    private static class Producer extends Thread {
        private final SharedStock sharedStock;

        public Producer(SharedStock sharedStock) {
            this.sharedStock = sharedStock;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    sharedStock.produce();
                    Thread.sleep(DELAY_MS*2);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Consumer Thread
    private static class Consumer extends Thread {
        private final SharedStock sharedStock;
        private final int consumerIndex;

        public Consumer(SharedStock sharedStock, int consumerIndex) {
            this.sharedStock = sharedStock;
            this.consumerIndex = consumerIndex;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    sharedStock.consume(consumerIndex);
                    Thread.sleep(DELAY_MS);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize shared stock
        SharedStock sharedStock = new SharedStock(500);
        sharedStock.printCurrentState();
        // Create Producer Thread
        Producer producer = new Producer(sharedStock);
        producer.start();

        // Create Consumer Threads
        Consumer[] consumers = new Consumer[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Consumer(sharedStock, i + 1);
            consumers[i].start();
        }

        // Let the simulation run for a while
        Thread.sleep(30000); // Run for 30 seconds

        // Interrupt all threads
        producer.interrupt();
        for (Consumer consumer : consumers) {
            consumer.interrupt();
        }
    }
}
