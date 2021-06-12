import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Commander {
    String argument = "";
    Vector<Ticket> TicketCollection;
    boolean exitStatus = false;
    final boolean fromScript;
    private final DataOutputStream outputStream;
    private CommandType type = CommandType.help;
    Ticket ticket;
    Logger logger = Logger.getLogger("server.command");
    public Commander(DataOutputStream outputStream, String argument, Ticket ticket, Vector<Ticket> TicketCollection, boolean fromScript) {
        this.outputStream = outputStream;
        this.argument = argument;
        this.TicketCollection = TicketCollection;
        this.fromScript = fromScript;
        this.ticket = ticket;
    }
    public void ArgumentChanger(String argument) {
        this.argument = argument;
    }
    public void TypeChanger(CommandType type) {
        this.type = type;
    }
    public void run() throws IOException, ParserConfigurationException {
        logger.info("running command");
        try {
            switch (type) {
                case help: this.help();break;
                case info: this.info();break;
                case show: this.show();break;
                case clear: this.clear();break;
                case exit: this.exit();break;
                case add: this.add();break;
                case insert_at: this.insert_at();break;
                case add_if_min: this.add_if_min();break;
                case average_price: this.average_price();break;
                case print_unique_event: this.print_unique_event();break;
                case count_by_price: this.count_by_price();break;
                case update: this.update();break;
                case remove_by_id: this.remove_by_id();break;
                case shuffle: this.shuffle();break;
                case mode: this.mode();break;
            }
        }   catch (NullPointerException ignored) {}
    }
    public void mode() throws IOException {
        System.out.println("here");
        logger.info("'mode' command trying to get access");
        boolean isMarker = false;
        for (Ticket ticket : TicketCollection) { if (ticket.getMarker()) isMarker = true;}
        if (isMarker) {
            outputStream.writeUTF("not connected yet");
            logger.info("Not connected yet, answer sent");
        } else {
            outputStream.writeUTF("connection set!");
        }
    }

    public void help() throws IOException {
        logger.info("'help' command was detected");
        outputStream.writeUTF("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n"+
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n"+
                "remove_by_id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n"+  "exit : закончить сессию\n" +
                "save : сохранить коллекцию в файл\n"+
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n"+
                "insert_at index {element} : добавить новый элемент в заданную позицию\n"+
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n"+
                "shuffle : перемешать элементы коллекции в случайном порядке\n"+
                "average_of_price : вывести среднее значение поля price для всех элементов коллекции\n"+
                "count_by_price price : вывести количество элементов, значение поля price которых равно заданному\n"+
                "print_unique_event : вывести уникальные значения поля event всех элементов в коллекции\n");
    }
    public void info() throws IOException {
        logger.info("'info' command was detected");
        outputStream.writeUTF("type = Vector of Tickets \n Number of items = " + TicketCollection.size());
        logger.info("Answer was sent");
    }
    public void show() throws IOException {
        logger.info("'show' command was detected");
        String description = "";
        for (Ticket ticket : TicketCollection) {
            description += extendedDescription(ticket) + "\n";
        }
        outputStream.writeUTF(description);
        logger.info("Answer was sent");
    }
    public void clear() throws IOException {
        logger.info("'clear' command was detected");
        TicketCollection.clear();
        outputStream.writeUTF("cleared");
    }
    public void shuffle() throws IOException {
        logger.info("'shuffle' command was detected");
        Collections.shuffle(TicketCollection);
        outputStream.writeUTF("Collection was shuffled");
    }
    public void exit() throws IOException {
        logger.info("'exit' command was detected");
        outputStream.writeUTF("This session finished");
        save();
        exitStatus = true;
        outputStream.flush();
    }

    public void print_unique_event() throws IOException {
        logger.info("'print_unique_event' command was detected");
        List<String> unique = new LinkedList<>();
        for (Ticket ticket:TicketCollection){
            if (!unique.contains(ticket.event.getEventType().toString())){
                unique.add(ticket.event.getEventType().toString());
            }
        }
        Set<String> uniqueElement = new HashSet<String>(unique);
        outputStream.writeUTF("Unique elements of eventType: " + uniqueElement.toString());
        logger.info("Answer was sent");
    }
    public void add() throws IOException {
        logger.info("'add' command was detected");
        TicketCollection.add(ticket);
        outputStream.writeUTF("new Ticket element was added");
        logger.info("Answer was sent");
    }

    public void remove_by_id() throws IOException {
        logger.info("'remove_by_id' command was detected");
        try {
            int id_argument = Integer.parseInt(argument);
            if (TicketCollection.stream().map(Ticket::getId).anyMatch(id -> id == id_argument)) {
                TicketCollection.removeIf(d -> d.getId() == id_argument);
                outputStream.writeUTF("Element(s) has been removed");
                logger.info("Answer was sent");
            } else {
                outputStream.writeUTF("No such element in TicketCollection");
                logger.info("Answer was sent");
            }
        } catch (NumberFormatException e){
            outputStream.writeUTF("Invalid argument. Try again");
            logger.info("Answer was sent");
        }
    }
    public void insert_at() throws IOException {
        logger.info("'insert_at' command was detected");
        try {
            int place = Integer.parseInt(argument);
            if (TicketCollection.size()>=place) {
                TicketCollection.insertElementAt(ticket , place);
                outputStream.writeUTF("Element(s) has been inserted");
                logger.info("Answer was sent");
            } else {
                outputStream.writeUTF("No such element in TicketCollection");
                logger.info("Answer was sent");
            }
        } catch (NumberFormatException e){
            outputStream.writeUTF("Invalid argument. Try again");
            logger.info("Answer was sent");
        }
    }
    public void update() throws IOException {
        logger.info("'update' command was detected");
        try {
            int id_argument = Integer.parseInt(argument);
            if (TicketCollection.stream().map(Ticket::getId).anyMatch(id -> id == id_argument)) {
                TicketCollection.stream().filter(d -> d.getId() == id_argument).forEach(d -> d.update(ticket));
                outputStream.writeUTF("Ticket has been updated");
                logger.info("Answer was sent");
            } else {
                outputStream.writeUTF("No such element id in TicketCollection. Try 'show' to see available id's");
                logger.info("Answer was sent");
            }

        } catch (NumberFormatException e){
            outputStream.writeUTF("Invalid argument. Try again");
            logger.info("Answer was sent");
        }
    }

    public void add_if_min() throws IOException {
        logger.info("'add_if_min' command was detected");
        if (TicketCollection.stream().map(Ticket::getPrice).allMatch(price -> price < ticket.getPrice())) {
            TicketCollection.add(ticket);
            outputStream.writeUTF("new Dragon has been added");
        }
        else {
            outputStream.writeUTF("new Dragon has NOT been added");
        }
        logger.info("Answer was sent");
    }


    public void average_price() throws IOException{
        logger.info("'average_price' command was detected");
        double x = 0;
        for (Ticket ticket:TicketCollection){
            x+=ticket.getPrice();
        }
        double average = x/TicketCollection.size();
        String average_price = Double.toString(average);
        outputStream.writeUTF(average_price);
    }

    public void count_by_price() throws IOException {
        try {
            double arg_price = Double.parseDouble(argument);
            if (TicketCollection.stream().map(Ticket::getPrice).anyMatch(price -> price == arg_price)) {
                double output = TicketCollection.stream().filter(d -> d.getPrice() == arg_price).count();
                String nums = Double.toString(output);
                outputStream.writeUTF(nums);
            } else {
                outputStream.writeUTF("No such elements");
                logger.info("Answer was sent");
            }
            logger.info("'count_by_price' command was detected");
            logger.info("Answer was sent");
        } catch (NumberFormatException e){
            outputStream.writeUTF("Invalid argument. Try again");
            logger.info("Answer was sent");
        }
    }
    public void save() throws  IOException {
        logger.info("saving to disk");
        File Output = null;
        Scanner scan = null;

        try {
            Output = new File(System.getenv("Output"));      // проверка на наличие переменной окружения
            scan = new Scanner(Output);
        } catch (NullPointerException e) {
            System.out.println("Cant find env variable");
            System.exit(0);
        }catch (FileNotFoundException e) {   // неправильный путь к файлу или нет доступа на чтение
            System.out.println("File not found");
            System.exit(0);
        }
        try {
            FileWriter writter = new FileWriter(Output);
            writter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<Ticket>\n");
            for (Ticket ticket : TicketCollection){
                String id =  ticket.getId().toString();
                String name = ticket.getName();
                String coordinates = ticket.getCoords().getX() + " " + ticket.getCoords().getY();
                String idevent = ticket.event.getIdTicket().toString();
                String eventname = ticket.event.getNameTicket();
                String eventage = ticket.event.getMinAge().toString();
                String eventcount = ticket.event.getTicketsCount().toString();
                String eventtype = ticket.event.getEventType().toString();
                String ticketdate = ticket.getCreationDate().toString();
                String typeField = ticket.type.toString();
                String price = Double.toString(ticket.getPrice());
                writter.write("<ticket " + "id='"+id+"'"+" name='"+name+"' "+"coordinates='"+coordinates+"' "+"eventid='"+idevent+"' "
                        + "eventname='"+eventname+"' "+" eventage='"+eventage+"' "+" eventcount='"+eventcount+"' "+" eventtype='"+eventtype+"' "
                        +" creation_date='"+ticketdate+"' "+" type='"+typeField+"' "+" price='"+price+"' "+" />\n");
            }
            writter.write("</Ticket>");
            writter.close();
            System.out.println("The command was executed");

            logger.info("Collection was saved to disk");
        } catch (NullPointerException e) {
            logger.info("Collection was not saved to disk");
        }
    }

    /**
     * Method used when 'show' command is called
     * @param ticket ticket which description need to be shown
     * @return String description
     */
    public static String extendedDescription(Ticket ticket) {
        return Stream.of(ticket.getId(), ticket.getCoords().getX(), ticket.getCoords().getY(), ticket.getName(),
                ticket.getPrice(), ticket.getCreationDate(), ticket.getEvent().getTicketsCount(), ticket.getEvent().getIdTicket(),
                ticket.getEvent().getEventType(), ticket.getEvent().getMinAge(), ticket.getEvent().getNameTicket()).map(Object::toString).collect(Collectors.joining(", "));
    }

    public enum CommandType {
        help,info,show,add, update,remove_by_id,clear,save,insert_at,execute_script, add_if_min,
        shuffle,average_price,count_by_price,print_unique_event ,exit, mode
    }
}