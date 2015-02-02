package scheduler;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static scheduler.Utils.*;

public class Room {
	public int id;
	
	public int capacity;
	public TimeBlock[] timeBlock;
	
	public Room(JSONObject jsonObj) throws JSONException {
		this.id = jsonObj.getInt("id");
		this.capacity = jsonObj.getInt("capacity");
		//this.timeBlock = TimeBlock.parseTimeBlock(jsonObj.getJSONArray("times"));
	}
	
	public Room(int _id, int _capacity, TimeBlock[] _timeBlock) {
		this.id = _id;
		this.capacity = _capacity;
		this.timeBlock = _timeBlock;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("\n\tID: %d, capacity: %d", this.id, this.capacity);
		for (TimeBlock block : timeBlock) {
			formatter.format("\n\t%s: %s - %s", block.getDay(),
					block.getTime(block.startTime), block.getTime(block.endTime));
		}
		formatter.close();
		return sb.toString();
	}
	
	public static Room[] parseRooms(List<JSONObject> jsonObj) throws JSONException {
		// TODO: Fill up parseRooms method
		Room[] rooms = new Room[jsonObj.size()];
		for (int i = 0; i < rooms.length; i++) {
			rooms[i] = new Room(jsonObj.get(i));
		}
		return rooms;
	}

	/**
	 * @param args
	 * @throws JSONException
	 */
	public static void main(String[] args) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObj = new JSONObject(readInput(System.in));
		JSONArray jsonResources = jsonObj.getJSONArray("resources");
		ArrayList<JSONObject> jsonRooms = new ArrayList<JSONObject>();
		int n = jsonResources.length();
		for (int i = 0; i < n; i++) {
			jsonObj = jsonResources.getJSONObject(i);
			if (jsonObj.getString("type").equals("room")) {
				jsonRooms.add(jsonObj);
			}
		}
		Room[] rooms = parseRooms(jsonRooms);
		
		for (Room room : rooms) {
			System.out.println(room);
		}
	}

}
