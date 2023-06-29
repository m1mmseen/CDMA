class AsciiTest {
    public static void main(String[] args) {
        String string = new String("t");
        byte[] arr = string.getBytes(StandardCharset.US_ASCII);
        System.out.println(arr.toString());
    }
}