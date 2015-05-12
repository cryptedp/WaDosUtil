package ch.judos.generic.data.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import ch.judos.generic.data.StringUtils;

/**
 * Use a static method to read data from an inputstream or a file. Then use the
 * getters to access the data from the csv-file.
 * 
 * @since 04.01.2012
 * @author Julian Schelker
 * @version 1.0 / 04.01.2012
 */
public class CSVFileReader {

	/**
	 * if it isn't muted, the readFile operations output some information about
	 * the csv file
	 */
	public static boolean	muted	= true;

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readFile(File input) throws IOException {
		return read(new FileReader(input));
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readStream(InputStream input) throws IOException {
		return read(new InputStreamReader(input));
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param input
	 *            the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader read(Reader input) throws IOException {
		BufferedReader reader = new BufferedReader(input);
		CSVFileReader result = new CSVFileReader(reader);
		return result;
	}

	/**
	 * a static implementation to read a csv-file
	 * 
	 * @param pathname
	 *            the filename of the file to read
	 * @return the interface to interact with the csv-file
	 * @throws IOException
	 */
	public static CSVFileReader readFile(String pathname) throws IOException {
		return readFile(new File(pathname));
	}

	/**
	 * @return an array of all existing attributes in this csv file
	 */
	public String[] getAttributes() {
		return this.attributes;
	}

	/**
	 * @return number of objects stored in the file
	 */
	public int countEntries() {
		return this.entities.length;
	}

	/**
	 * @param index
	 *            must be in the valid range (between 0 and countEntries()-1)
	 * @return a stored object of the file
	 * @throws IndexOutOfBoundsException
	 */
	public String[] getEntry(int index) throws IndexOutOfBoundsException {
		return this.entities[index];
	}

	protected String	separation;
	private String[]	attributes;
	private String[][]	entities;

	/**
	 * reads and parses a csv file
	 * 
	 * @param reader
	 * @throws IOException
	 */
	protected CSVFileReader(BufferedReader reader) throws IOException {
		String line;
		String attributeLine = reader.readLine();
		HashMap<String, Integer> candidates = getSeparationCandidates(attributeLine);
		ArrayList<String> entityList = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			entityList.add(line);
		}
		removeSeparationCandidates(candidates, entityList);
		this.separation = getSeparationCharacter(candidates);

		this.attributes = CSVFile.decodeForValue(attributeLine.split(this.separation, -1));
		this.entities = new String[entityList.size()][];
		int index = 0;
		for (String entity : entityList) {
			this.entities[index] = CSVFile.decodeForValue(entity.split(this.separation, -1));
			index++;
		}
		if (!muted) {
			System.out.println("Separation character = " + this.separation);
			System.out.println("Number of entities = " + entityList.size());
			System.out.println("Number of attributes = " + this.attributes.length);
		}
	}

	/**
	 * @param candidates
	 * @return
	 * @throws IOException
	 */
	protected String getSeparationCharacter(HashMap<String, Integer> candidates)
		throws IOException {
		for (String c : candidates.keySet())
			return c;
		throw new IOException("No separation candidates left, check file content.");
	}

	/**
	 * @param candidates
	 * @param entities
	 */
	protected void removeSeparationCandidates(HashMap<String, Integer> candidates,
		ArrayList<String> entities) {
		for (String entity : entities) {
			Iterator<Entry<String, Integer>> it = candidates.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> en = it.next();
				int noc = StringUtils.countMatches(entity, en.getKey());
				if (noc != en.getValue()) {
					it.remove();
					if (candidates.size() == 1)
						return;
				}
			}
		}

	}

	/**
	 * finds all possible separation chars
	 * 
	 * @param attributes
	 * @return
	 */
	protected HashMap<String, Integer> getSeparationCandidates(String attributes) {
		HashMap<String, Integer> result = new HashMap<>();
		String s;
		for (char c : attributes.toCharArray()) {
			s = Character.toString(c);
			if (result.containsKey(s))
				result.put(s, result.get(s) + 1);
			else
				result.put(s, 1);
		}
		return result;
	}

}
