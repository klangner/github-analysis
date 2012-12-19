package com.matrobot.gha.archive.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * This reader will filter events based on given parameters
 * 
 * @author Krzysztof Langner
 */
public class OrderedRepoReader implements IRepositoryReader{

	public static final int SORT_BY_EVENTS = 1;
	public static final int SORT_BY_EVENTS_DESC = 2;
	public static final int SORT_BY_FORKS = 3;
	public static final int SORT_BY_FORKS_DESC = 4;
	public static final int SORT_BY_NAME = 5;
	
	private IRepositoryReader reader;
	private List<RepositoryRecord> index = null;
	private Iterator<RepositoryRecord> indexIterator = null;
	private int field = SORT_BY_EVENTS;
	
	
	public OrderedRepoReader(IRepositoryReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * SORT_BY_*
	 */
	public void addField(int field){
		this.field = field;
	}
	
	
	@Override
	public RepositoryRecord next(){

		if(index == null){
			initIndex();
		}
		
		RepositoryRecord record = null;
		if(indexIterator.hasNext()){
			record = indexIterator.next();
		}
		
		return record;
	}


	/**
	 * Add repositories to the index
	 */
	private void initIndex() {

		index = new ArrayList<RepositoryRecord>();
		RepositoryRecord record;
		Comparator<RepositoryRecord> cmp = getComparator();
		
		while((record=reader.next()) != null){
			int pos = Collections.binarySearch(index, record, cmp);
			if (pos<0) { // not found
				index.add(-pos-1, record);
			}			
			else{
				index.add(pos, record);
			}
		}
		
		indexIterator = index.iterator();
	}
	
	/**
	 * Get comparator based on order field
	 * @param orderBy
	 * @return
	 */
	private Comparator<RepositoryRecord> getComparator() {

		Comparator<RepositoryRecord> cmp = null;
		
		if(field == SORT_BY_FORKS){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.forkEventCount-o2.forkEventCount;
				}
			};
		}
		else if(field == SORT_BY_FORKS_DESC){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o2.forkEventCount-o1.forkEventCount;
				}
			};
		}
		else if(field == SORT_BY_NAME){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.repository.compareTo(o2.repository);
				}
			};
		}
		else if(field == SORT_BY_EVENTS){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.eventCount-o2.eventCount;
				}
			};
		}
		else if(field == SORT_BY_EVENTS_DESC){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o2.eventCount-o1.eventCount;
				}
			};
		}
		
		return cmp;
	}
	
}
