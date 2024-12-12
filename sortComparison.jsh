int cardCompare(String card1, String card2) {
    Map<Character, Integer> suitPriority = new HashMap<>();
    suitPriority.put('H', 1);
    suitPriority.put('C', 2);
    suitPriority.put('D', 3);
    suitPriority.put('S', 4);
    int number1 = Integer.parseInt(card1.substring(0, card1.length() - 1));
    char suit1 = card1.charAt(card1.length() - 1);
    int number2 = Integer.parseInt(card2.substring(0, card2.length() - 1));
    char suit2 = card2.charAt(card2.length() - 1);
    if (suitPriority.get(suit1) < suitPriority.get(suit2)) {
        return -1;
    } else if (suitPriority.get(suit1) > suitPriority.get(suit2)) {
        return 1;
    } else {
        if (number1 < number2) {
            return -1;
        } else if (number1 > number2) {
            return 1;
        } else {
            return 0;
        }
    }
}

ArrayList<String> bubbleSort(ArrayList<String> array) {
        int n = array.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (cardCompare(array.get(j), array.get(j + 1)) > 0) {
                    String temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return array;
}

ArrayList<String> mergeSort(ArrayList<String> array) {
        if (array.size() <= 1) {
            return array;
        }
        int mid = array.size() / 2;
        ArrayList<String> left = new ArrayList<>(array.subList(0, mid));
        ArrayList<String> right = new ArrayList<>(array.subList(mid, array.size()));
        left = mergeSort(left);
        right = mergeSort(right);
        ArrayList<String> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (cardCompare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }
        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }
        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }
        return result;
}

long measureBubbleSort(String filename) throws IOException {
        List<String> cards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                cards.add(line.trim());
            }
        }
        long startTime = System.currentTimeMillis();
        bubbleSort(new ArrayList<>(cards));
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
}

long measureMergeSort(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        ArrayList<String> cards = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            cards.add(line.trim());
        }
        reader.close();
        long startTime = System.nanoTime();
        mergeSort(cards);
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1_000_000;
}

void sortComparison(String[] filenames) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("sortComparison.csv"));
    writer.write(", ");
    for (String filename : filenames) {
        String numCards = filename.replaceAll("[^0-9]", "");
        writer.write(numCards + ", ");
    }
    writer.newLine();
    long[] bubbleSortTimes = new long[filenames.length];
    long[] mergeSortTimes = new long[filenames.length];
    for (int i = 0; i < filenames.length; i++) {
        String filename = filenames[i];
        bubbleSortTimes[i] = measureBubbleSort(filename);
        mergeSortTimes[i] = measureMergeSort(filename);
    }
    writer.write("bubbleSort, ");
    for (long time : bubbleSortTimes) {
        writer.write(time + ", ");
    }
    writer.newLine();
    writer.write("mergeSort, ");
    for (long time : mergeSortTimes) {
        writer.write(time + ", ");
    }
    writer.newLine();
    writer.close();
    BufferedReader reader = new BufferedReader(new FileReader("sortComparison.csv"));
    String line;
    System.out.println("sortComparison.csv has been saved on the computer, and its content is:");
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
    reader.close();
}