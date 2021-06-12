import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;


public class Ticket implements Serializable {

    private Integer id;
    private String  name;
    public Coordinates coordinates;
    public Event event;
    private double price ;
    public TicketType type;
    private LocalDateTime creationDate;
    public boolean marker = false;
    private static final long serialVersionUID = 18L;

    public Ticket(){
        this.id = 1;
        this.name = "default";
        this.coordinates = new Coordinates(1.0, 5L);
        this.event = new Event();
        this.price = 1;
        this.creationDate = LocalDateTime.now();
        this.type = TicketType.USUAL;
    }
    public Ticket(Integer id, String name, Coordinates coordinates, Event e, double price, String t, LocalDateTime creationDate) {//, , TicketType t
        this.id = id;
        if (id == null) this.id = new Random().nextInt();

        if ((name != null) && (name.length() != 0)) {
            this.name = name;
        } else {
            throw new IncorrectFieldException("Incorrect input name");
        }

        if (coordinates != null) {
            this.coordinates = coordinates;
        } else {
            throw new IncorrectFieldException("Incorrect input coordinates");
        }

        this.creationDate = creationDate;
        if (creationDate == null) this.creationDate = LocalDateTime.now();

        if (price > 0) {
            this.price = price;
        } else {
            throw new IncorrectFieldException("Incorrect input price (need to be >0)");
        }

        switch (t){
            case "BUDGETARY":
                this.type = TicketType.BUDGETARY;
                break;
            case "CHEAP":
                this.type = TicketType.CHEAP;
                break;
            case "USUAL":
                this.type = TicketType.USUAL;
                break;
            case "VIP":
                this.type = TicketType.VIP;
                break;
            default: throw new IncorrectFieldException("Incorrect input TicketType");
        }

        if (event != null) {
            this.event = e;
        } else {
            throw new IncorrectFieldException("Incorrect input event");
        }

    }
    public Ticket(boolean marker) {
        super();
        this.marker = marker;
    }
    public Integer getId() {
        return id;
    }
    public double getPrice(){
        return price;
    }
    public String getName() {
        return name;
    }
    public boolean getMarker() {return marker; }
    public java.time.LocalDateTime getCreationDate() {
        return creationDate;
    }
    public Coordinates getCoords() {
        coordinates.getX();
        coordinates.getY();
        return coordinates;
    }
    public TicketType getType(){
        return type;
    }
    public Event getEvent(){
        event.getEventType();
        event.getIdTicket();
        event.getMinAge();
        event.getNameTicket();
        event.getTicketsCount();
        return event;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public void update(Ticket ticket){
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.coordinates = ticket.getCoords();
        this.event = ticket.getEvent();
        this.type = ticket.getType();
        this.creationDate = ticket.getCreationDate();
        this.price = ticket.getPrice();
        this.price = ticket.getPrice();
    }
    @Override
    public String toString() {
        return "["+id+" " +name+" " + " "+coordinates.getX()+" "+ coordinates.getY()+" " + " " +event.getIdTicket() + " " + event.getNameTicket() + " " + event.getMinAge()+ " " + event.getTicketsCount() + " " + event.getEventType() + " " + price + " "+ type + "]";
    }
}
class Event implements Serializable{
    private int id;
    private String name;
    private int minAge;
    private long ticketsCount;
    private EventType eventType;
    private static final long serialVersionUID = 18L;

    public Event(){
        this.id = 1;
        this.name = "name";
        this.minAge = 1;
        this.ticketsCount =1;
        this.eventType = EventType.OPERA;
    }
    public Event(Integer id, String name, int minAge, long ticketsCount, String s){
        this.id = id;
        if (id == null) this.id = new Random().nextInt();


        if ((name != null) && (name.length() != 0)) {
            this.name = name;
        } else {
            throw new NumberFormatException();
        }

        this.minAge = minAge;

        if (ticketsCount > 0) {
            this.ticketsCount = ticketsCount;
        } else {
            throw new NumberFormatException();
        }
        switch (s){
            case "OPERA":
                this.eventType = EventType.OPERA;
                break;
            case "CONCERT":
                this.eventType = EventType.CONCERT;
                break;
            case "FOOTBALL":
                this.eventType = EventType.FOOTBALL;
                break;
            case "BASKETBALL":
                this.eventType = EventType.BASKETBALL;
                break;
            case "THEATRE_PERFORMANCE":
                this.eventType = EventType.THEATRE_PERFORMANCE;
                break;
            default: throw new NumberFormatException();
        }


    }

    public Integer getIdTicket(){return id;}
    public String getNameTicket(){return name;}
    public Integer getMinAge(){return minAge;}
    public int getMinAge2(){return minAge;}
    public Long getTicketsCount(){return ticketsCount;}
    public EventType getEventType() {
        return eventType;
    }
    public void setIdEvent(Integer id){
        this.id = id;
    }
}
class Coordinates implements Serializable {

    public Double x;
    public Long y;
    private static final long serialVersionUID = 18L;

    public Coordinates(Double x, Long y) {
        if (x != null) {
            this.x = x;
        } else {
            throw new IncorrectFieldException("Incorrect input X");
        }
        if (y != null) {
            this.y = y;
        } else {
            throw new IncorrectFieldException("Incorrect input Y");
        }
    }

    public Double getX() {
        return x;
    }

    public Long getY() {
        return y;
    }



    @Override
    public String toString() {
        return "x:" + x +
                ", y:" + y;
    }
}
enum  EventType implements Serializable {
    CONCERT,
    FOOTBALL,
    BASKETBALL,
    OPERA,
    THEATRE_PERFORMANCE;
}
enum TicketType implements Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP;
}