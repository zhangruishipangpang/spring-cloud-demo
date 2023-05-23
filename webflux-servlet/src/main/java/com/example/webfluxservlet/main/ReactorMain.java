//package com.example.webfluxservlet.main;
//
//import org.reactivestreams.Subscription;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//import reactor.core.Exceptions;
//import reactor.core.publisher.BaseSubscriber;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.util.retry.Retry;
//
//import java.time.Duration;
//import java.time.LocalTime;
//import java.util.Comparator;
//import java.util.concurrent.Flow;
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @Auther: 长安
// */
//public class ReactorMain {
//
//    public static void main(String[] args) {
//
////        Flux<String> flux = Flux.fromArray(new String[]{"a"});
////        flux.subscribe()
////        Flux.interval(Duration.ZERO).subscribe(System.out::println);
//
////        BaseSubscriber<Integer> subscriber = new BaseSubscriber<Integer>() {
////            @Override
////            protected Subscription upstream() {
////                return super.upstream();
////            }
////
////            @Override
////            protected void hookOnSubscribe(Subscription subscription) {
////                System.out.println(" hookOnSubscribe ");
////                request(1);
////            }
////
////            @Override
////            protected void hookOnNext(Integer value) {
////                System.out.println(" receive : " + value);
////                request(1);
////            }
////        };
////
////        Flux<Integer> range = Flux.range(1, 10);
////        range.subscribe(subscriber);
//
////        String[] sources = new String[]{"1","2","3","4","5","6"};
////        Flux.fromArray(sources)
////            .concatMap(source -> Mono.just(source + "change"))
////            .filter(f -> f.equals("7"))
////            .next()
////            .switchIfEmpty(createNotFoundError())
////            .flatMap(source -> Mono.just(source + " change2"))
////            .flatMap(source -> Mono.just(source + " change3"))
////            .subscribe(System.out::println)
//        ;
//
////        AtomicInteger errorCount = new AtomicInteger();
////        Flux<String> flux =
////            Flux.<String>error(new IllegalArgumentException())
////                .doOnError(e -> errorCount.incrementAndGet())
////                .retryWhen(Retry.from(companion ->
////                    companion.map(rs -> {
////                        if (rs.totalRetries() < 3) {
////                            System.out.println(" --> 重试：" + rs.totalRetries());
////                            return rs.totalRetries();
////                        }
////                        else throw Exceptions.propagate(rs.failure());
////                    })
////                ));
////        flux.subscribe();
//
////        AtomicInteger errorCount = new AtomicInteger();
////        Flux<String> flux =
////            Flux.<String>error(new IllegalStateException("boom"))
////                .doOnError(e -> {
////                    errorCount.incrementAndGet();
////                    System.out.println(e + " at " + LocalTime.now());
////                })
////                .retryWhen(Retry
////                    .backoff(3, Duration.ofMillis(100)).jitter(0d)
////                    .doAfterRetry(rs -> System.out.println("retried at " + LocalTime.now() + ", attempt " + rs.totalRetries()))
////                    .onRetryExhaustedThrow((spec, rs) -> rs.failure())
////                );
////        flux.subscribe();
//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static <R> Mono<R> createNotFoundError() {
//        return Mono.defer(() -> {
//            Exception ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "No matching handler");
//            return Mono.error(ex);
//        });
//    }
//
//}
