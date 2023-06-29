
public class CDMA {
    static class Chip {
        private int[] S = new int[4];
        public Chip(int s1, int s2, int s3, int s4) {
            S[0] = s1;
            S[1] = s2;
            S[2] = s3;
            S[3] = s4;
        }

        public int get(int index) {
            return S[index];
        }
    }

    static class BitMessage {

        private int[] message = new int[4];

        @Override public String toString() {
            StringBuilder s = new StringBuilder("(");
            for (int i = 0; i < 4; i++) {
                if (i != 0) {
                    s.append(", ");
                }
                s.append(String.valueOf(message[i]));
            }
            s.append(")");
            return s.toString();
        }

        public BitMessage encode (Chip c, boolean bit) {

            for (int s = 0; s < 4; s++) {
                if (!bit) {
                    switch (c.get(s)) {
                        case 1 -> message[s] -= 1;
                        case -1 -> message[s] += 1;
                    }
                } else {
                    message[s] += c.get(s);
                }
            }
            return this;
        }

        public  boolean decode(Chip c) {
            int sum = 0;
            for (int i = 0; i < 4; i++) {
                sum += c.get(i) * message[i];
            }
            double b = sum;
            sum = (int) (0.25 * b);
            return switch (sum) {
                case 1 -> true;
                case -1 -> false;
                default -> false;
            };
        }
    }

    static class ByteMessage {
        private BitMessage[] bits = new BitMessage[8];
        public ByteMessage() {

        }

        @Override public String toString() {

        }

        public ByteMessage encode(Chip chip, char c) {
            String string = String.valueOf(c);
            byte[] bytes = string.getBytes();
            StringBuilder binary = new StringBuilder();
            for (byte b : bytes) {
                int val = b;
                for (int i = 0; i < 8; i++) {
                    binary.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
            }

        }
        public char decode(Chip chip) {

        }

    }

    public static void main(String[] args) {
        var A = new Chip(1, 1, 1, 1);
        var B = new Chip(1, -1, 1, -1);
        var C = new Chip(1, 1 ,-1, -1);
        var D = new Chip(1, -1, -1, 1);

        var message = new BitMessage().encode(A, true).encode(B, false).encode(C, true).encode(D, false);
        System.out.println(message);
        System.out.println(message.decode(A) + " " + message.decode(B) + " " + message.decode(C) + " " + message.decode(D));

        var messageByte = new ByteMessage().encode(A, 't').encode(B, 'e').encode(C, 's').encode(C, 't');
        System.out.println(messageByte);
        System.out.println("" + messageByte.decode(A) + messageByte.decode(B) + messageByte.decode(C) + messageByte.decode(D));
    }

}
