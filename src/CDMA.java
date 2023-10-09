
public class CDMA {
    static class Chip {
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

        public static final int MESSAGE_SIZE = 4;
        private int[] message = new int[MESSAGE_SIZE];

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder("(");
            for (int i : message) {
                if (s.length() > 1) {
                    s.append(", ");
                }
                s.append(i);
            }
            s.append(")");

            return s.toString();
        }

        public BitMessage encode(Chip c, boolean bit) {


            for (int s = 0; s < MESSAGE_SIZE; s++) {
                message[s] += bit ? c.get(s) : -c.get(s);
            }
            return this;
        }

        public boolean decode(Chip c) {
            int sum = 0;
            for (int i = 0; i < MESSAGE_SIZE; i++) {
                sum += c.get(i) * message[i];
            }
            sum >>= 2;
            return sum == 1;
        }
    }

    static class ByteMessage {
        private BitMessage[] bits = new BitMessage[8];

        //In the constructor, the inner BitMessage arrays are inserted to avoid a NullPointerException
        public ByteMessage() {
            for (int i = 0; i < 8; i++) {
                bits[i] = new BitMessage();
            }
        }
        // To generate the correct format for the output, the method of the Object class is overridden.
        @Override
        public String toString() {

            StringBuilder s = new StringBuilder();
            for (BitMessage bitMessage : bits) {
                if (s.length() > 0) {
                    s.append(" ");
                }
                s.append(bitMessage);
            }
            return s.toString();
        }

    // To encode the ByteMessage, we firstly rely on the method from the BitMessage class. On the other hand, the specific bit of the ASCII character at position 128 is compared. The corresponding value (query result true or false, i.e., 1, -1) is then passed to the BitMessage encode method as the second parameter. Afterward, using bit shifting, the next position of the bit code is "shifted" to position 128 and can be treated in the same manner
        public ByteMessage encode(Chip chip, char c) {
            int input = c;
            for (int i = 7; i >= 0; i--) {
                bits[i].encode(chip, (input & 128) != 0);
                input <<= 1;
            }
            return this;

        }


        //Complementary to the encode method, we again work with a bitwise operator. However, this time a value isn't being read, but rather being "written." Using the BitMessage decode method, the boolean value is determined. If this value is "True," a one is "written" at the corresponding bit code position. If not, the loop iteration ends and at the beginning of the new iteration, a bit shift occurs.
            public char decode(Chip chip) {
            int output = 0;

            for (int i = 7; i >= 0; i--) {
                output <<= 1;
                if(bits[i].decode(chip)) {
                    output |= 1;
                }
            }
            return (char) output;
        }

                //ALTERNATIV :)
//        public char decode(Chip chip) {
//            char output = 0;
//            int bit = 128;
//            for (int i = 7; i >= 0; i--) {
//
//                if (bits[i].decode(chip)) {
//                    output += bit;
//                }
//                bit >>= 1;
//            }
//            return output;
//        }
    }

    public static void main(String[] args) {
        var A = new Chip(1, 1, 1, 1);
        var B = new Chip(1, -1, 1, -1);
        var C = new Chip(1, 1, -1, -1);
        var D = new Chip(1, -1, -1, 1);

        var message = new BitMessage().encode(A, true).encode(B, false).encode(C, true).encode(D, false);
        System.out.println(message);
        System.out.println(
                message.decode(A) + " " + message.decode(B) + " " + message.decode(C) + " " + message.decode(D));

        var messageByte = new ByteMessage().encode(A, 't').encode(B, 'e').encode(C, 's').encode(C, 't');
        System.out.println(messageByte);
        System.out.println(
                "" + messageByte.decode(A) + messageByte.decode(B) + messageByte.decode(C) + messageByte.decode(D));
    }

}
