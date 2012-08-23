package net.baquara.dbservices.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.baquara.dbservices.dbschema.Table;
import net.baquara.dbservices.dbschema.TableSchema;
import net.baquara.dbservices.exception.TableSchemaHasCyclesException;

/**
 * A {@link List} containing {@link Table} elements (List&lt;Table&gt;) with its
 * elements sorted topologically. If a Table in a given position i (let's call
 * it Table[i]) has some foreign key definition, then one of the tables Table[0]
 * to Table[i-1] must have the corresponding primary key definition. This class
 * is useful when creating tables or inserting backup data on the database
 * system, because if a table has a foreign key, we must have created the table
 * with the corresponding primary key before, otherwise we can get an error from
 * the database system.
 */
public class SortedTableList implements List<Table> {

	private final List<Table> sortedTableList;

	/**
	 * Class constructor.
	 * 
	 * @param tableSchema
	 * @throws TableSchemaHasCyclesException
	 *             See {@link TableSchemaHasCyclesException} why this error
	 *             occurs.
	 */
	public SortedTableList(TableSchema tableSchema)
			throws TableSchemaHasCyclesException {

		// The graph consists of a vertexList and an adjacency matrix
		LinkedList<Table> vertexList = new LinkedList<Table>(tableSchema
				.getTable());
		int size = vertexList.size();
		sortedTableList = new LinkedList<Table>();

		// Creating a list containing TableKeys of each table
		TableKeys[] tableKeys = new TableKeys[size];
		for (int i = 0; i < size; i++)
			tableKeys[i] = new TableKeys(vertexList.get(i), tableSchema);

		// Creating adjacency matrix and initializing it. A
		// table has a sucessor when vertexList[i] has a primary
		// key and vertexList[j] has the corresponding foreign
		// key. This will be represented as adjMatrix[i][j] = true
		boolean[][] adjacencyMatrix = new boolean[size][size];
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				TableKeys iKeys = tableKeys[row];
				TableKeys jKeys = tableKeys[col];

				if (row != col) {
					if (iKeys.getPrimaryKeyId() != null)
						if (jKeys.containForeignKeyByID(iKeys.getPrimaryKeyId()))
							adjacencyMatrix[row][col] = true;
				} else
					adjacencyMatrix[row][col] = false;
			}

		// Sorting table
		for (int numVerts = size; numVerts > 0; numVerts--) {

			// Get a vertex position with no successor
			int currentVertex = vertexWithNoSuccessors(adjacencyMatrix,	numVerts);

			// insert vertex label before all the others
			sortedTableList.add(0, vertexList.get(currentVertex));

			// removing vertex from list
			vertexList.remove(currentVertex);

			// removing enties from adjmatrix
			for (int row = currentVertex; row < numVerts - 1; row++)
				for (int col = 0; col < numVerts; col++)
					adjacencyMatrix[row][col] = adjacencyMatrix[row + 1][col];

			for (int col = currentVertex; col < numVerts - 1; col++)
				for (int row = 0; row < numVerts; row++)
					adjacencyMatrix[row][col] = adjacencyMatrix[row][col + 1];
		}
	}

	private int vertexWithNoSuccessors(boolean[][] adjacencyMatrix, int numVerts) throws TableSchemaHasCyclesException {
		// A row in an ajdacency martix indicates if the
		// vertex has successors. If all the columns in a
		// row are false, then the vertex has no successors.
		for (int row = 0; row < numVerts; row++) {
			boolean hasSuccessors = false;
			for (int col = 0; col < numVerts; col++)
				hasSuccessors = hasSuccessors || adjacencyMatrix[row][col];
			if (!hasSuccessors)
				return row;
		}
		// If all rows have successors, then we have a cycle
		throw new TableSchemaHasCyclesException();
	}

	@Override
	public void add(int index, Table element) {
		add(index, element);
	}

	@Override
	public boolean add(Table e) {
		return add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Table> c) {
		return sortedTableList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Table> c) {
		return sortedTableList.addAll(index, c);
	}

	@Override
	public void clear() {
		sortedTableList.clear();
	}

	@Override
	public boolean contains(Object o) {
		return sortedTableList.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return sortedTableList.containsAll(c);
	}

	@Override
	public Table get(int index) {
		return sortedTableList.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return sortedTableList.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return sortedTableList.isEmpty();
	}

	@Override
	public Iterator<Table> iterator() {
		return sortedTableList.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return sortedTableList.lastIndexOf(o);
	}

	@Override
	public ListIterator<Table> listIterator() {
		return sortedTableList.listIterator();
	}

	@Override
	public ListIterator<Table> listIterator(int index) {
		return sortedTableList.listIterator(index);
	}

	@Override
	public Table remove(int index) {
		return sortedTableList.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return sortedTableList.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return sortedTableList.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return sortedTableList.retainAll(c);
	}

	@Override
	public Table set(int index, Table element) {
		return sortedTableList.set(index, element);
	}

	@Override
	public int size() {
		return sortedTableList.size();
	}

	@Override
	public List<Table> subList(int fromIndex, int toIndex) {
		return sortedTableList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return sortedTableList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return sortedTableList.toArray(a);
	}

}
