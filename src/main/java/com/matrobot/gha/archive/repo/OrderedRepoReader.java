package com.matrobot.gha.archive.repo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	public static final int SORT_BY_PUSHES = 5;
	public static final int SORT_BY_PUSHES_DESC = 6;
	public static final int SORT_BY_COMMUNITY = 7;
	public static final int SORT_BY_COMMUNITY_DESC = 8;
	public static final int SORT_BY_NAME = 20;
	
	private IRepositoryReader reader;
	private List<RepositoryRecord> sortedRecords = null;
	private int field = SORT_BY_EVENTS;
	
	
	public OrderedRepoReader(IRepositoryReader reader){
		this.reader = reader;
	}
	
	
	/**
	 * SORT_BY_*
	 */
	public void setField(int field){
		this.field = field;
	}
	
	
	@Override
	public RepositoryRecord next(){

		if(sortedRecords == null){
			initIndex();
		}
		
		RepositoryRecord record = null;
		if(sortedRecords.size() > 0){
			record = sortedRecords.remove(0);
		}
		
		return record;
	}


	/**
	 * Add repositories to the index
	 */
	private void initIndex() {

		sortedRecords = new ArrayList<RepositoryRecord>();
		RepositoryRecord record;
		Comparator<RepositoryRecord> cmp = getComparator();
		
		while((record=reader.next()) != null){
			int pos = Collections.binarySearch(sortedRecords, record, cmp);
			if (pos<0) { // not found
				sortedRecords.add(-pos-1, record);
			}			
			else{
				sortedRecords.add(pos, record);
			}
		}
	}
	
	/**
	 * Get comparator based on order field
	 * @param orderBy
	 * @return
	 */
	private Comparator<RepositoryRecord> getComparator() {

		Comparator<RepositoryRecord> cmp = null;
		
		if(field == SORT_BY_EVENTS){
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
		else if(field == SORT_BY_FORKS){
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
		else if(field == SORT_BY_PUSHES){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.pushEventCount-o2.pushEventCount;
				}
			};
		}
		else if(field == SORT_BY_PUSHES_DESC){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o2.pushEventCount-o1.pushEventCount;
				}
			};
		}
		else if(field == SORT_BY_COMMUNITY){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.community.size()-o2.community.size();
				}
			};
		}
		else if(field == SORT_BY_COMMUNITY_DESC){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o2.community.size()-o1.community.size();
				}
			};
		}
		else if(field == SORT_BY_NAME){
			cmp = new Comparator<RepositoryRecord>() {
				public int compare(RepositoryRecord o1, RepositoryRecord o2) {
					return o1.repoName.compareTo(o2.repoName);
				}
			};
		}
		
		return cmp;
	}
	
}
