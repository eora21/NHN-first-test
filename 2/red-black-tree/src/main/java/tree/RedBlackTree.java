package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class RedBlackTree {
    public static void main(String[] args) {
        // 1. 정수를 저장하는 해당 클래스 인스턴스를 작성하세요.
        TreeSet<Integer> redBlackTree = new TreeSet<>();
        List<Integer> randomNumbers = new ArrayList<>();

        // 2. 객체에 30개의 무작위 정수를 추가하세요.
        Random random = new Random();

        for (int idx = 0; idx < 30; idx++) {
            int randomNumber = random.nextInt(100);
            randomNumbers.add(randomNumber);
            redBlackTree.add(randomNumber);
        }

        // 3. 삽입된 무작위 정수들을 삽입 순서대로 출력하세요.
        System.out.println(randomNumbers); // treeMap써서 구현했으나 수정, 삽입될 정수들을 출력하라고 하셨습니다.

        // 4. 삽입된 무작위 정수들을 내림 차순으로 정렬해서 출력하세요
        System.out.println(redBlackTree.descendingSet());
        // 만약 TreeSet으로 정해진 게 아니라면 values() 뽑아서 reverseOrder 후 출력

        // 5. 삽입된 무작위 정수들의 합을 구하는 메소드를 작성하고, 실행해서 출력하는 코드를 작성하세요.
        System.out.println(getTotalValues(redBlackTree));
    }

    public static long getTotalValues(Set<Integer> numbers) {
        return numbers.stream()
                .mapToLong(Long::valueOf) // 정수 범위가 너무 크다면 출력 시 int 최대값을 벗어날 수 있으므로 Long 변경
                .reduce(Long::sum)
                .orElse(0);
    }
}
