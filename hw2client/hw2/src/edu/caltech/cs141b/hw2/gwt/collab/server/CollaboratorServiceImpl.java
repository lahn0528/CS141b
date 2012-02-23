package edu.caltech.cs141b.hw2.gwt.collab.server;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;
import edu.caltech.cs141b.hw2.gwt.collab.client.CollaboratorService;
import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMessage;
import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockExpired;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockUnavailable;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LoginResults;
import edu.caltech.cs141b.hw2.gwt.collab.shared.StatusMessage;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.server.PMF;

import com.google.appengine.api.datastore.KeyFactory;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CollaboratorServiceImpl extends RemoteServiceServlet implements
		CollaboratorService {
	// Used for logging purposes
	private static final Logger log = Logger.getLogger(CollaboratorServiceImpl.class.toString());
	// Default time given to edit after locking a document
	static final int EDIT_WINDOW = 60000;
	
	/**
	 * Check list of documents, looking for any expired documents
	 */
	public static void clearLockReqeust() {
	    // Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query query = pm.newQuery(DocumentInfo.class);

		try {
			// Start transaction
			tx.begin();
			log.fine("Getting a list of currently available documents");
			
			// Get documents from query
			List<DocumentInfo> results = ((List<DocumentInfo>) query.execute());
			tx.commit();
			if (!results.isEmpty()) {
				for (DocumentInfo d: results) {
					// If document's lock expired, inform the next user.
					Date now = new Date();
					if (d.getLockedUntil() != null && now.after(d.getLockedUntil())) {
						tx.begin();
						d.removeFirstUser();
						InformNextUserHelper(d);
						tx.commit();
					}
				}
			}
			//tx.commit();
		} finally {
			query.closeAll();
		}   
	}
	
	/**
	 * Used to login a user
	 * @see edu.caltech.cs141b.hw2.gwt.collab.client.CollaboratorService#login(java.lang.String)
	 * 
	 * @param name : String given by user
	 * @return LoginResults object containing information for user to connect to channel
	 */
	public LoginResults login(final String name) {

    	// Set user to be the owner of the session
	    User user = new User();
	    user.setName(name);

	    // Store in the database to get a unique key for each user.
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Transaction tx = pm.currentTransaction();
	    try {
	      tx.begin();
	      pm.makePersistent(user);
	      tx.commit();
	    } 
	    finally {
	      if (tx.isActive()) {
	        tx.rollback();
	      }
	    }
	    
	    // Create channel and pass information back to user
	    String channelId = PushServer.createChannel(user);
	    return new LoginResults(user.getKey(), channelId);
	}
	
	/**
	 * Used to get a list of the currently available documents.
	 * 
	 * @return docsList : List of the metadata of the currently available documents
	 */
	public List<DocumentMetadata> getDocumentList() {
		// Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Query query = pm.newQuery(DocumentInfo.class);
		
		// Store list of the metadata of the current documents
		List<DocumentMetadata> docsList = new ArrayList<DocumentMetadata>();
		try {
			// Start transaction
			tx.begin();
			log.fine("Getting a list of currently available documents");
			
			// Get documents from query
			List<DocumentInfo> results = ((List<DocumentInfo>) query.execute());
			if (!results.isEmpty()) {
				
				// Add metadata of each document to docsList
				for (DocumentInfo d: results) {
					//pm.deletePersistent(d);
				    docsList.add(d.getDocumentMetdataObject());
				    System.out.println("getLockedBy: " + d.getLockedBy());
				}
			}
			
			tx.commit();
		} finally {
			query.closeAll();
		}
		return docsList;
	}

	/**
	 * Used to lock an existing document for editing.
	 * 
	 * @param documentKey : the key of the document to lock
	 * 		  userKey : the key of the user
	 * @return lockedDoc : LockedDocument object containing the current document state
	 * @throws LockUnavailable if a lock cannot be obtained
	 */
    public LockedDocument lockDocument(String documentKey, String userKey)
            throws LockUnavailable {
    	
    	// Instantiate JDO-aware application components
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        
        LockedDocument lockedDoc = null;
        try {
        	// Start transaction
            tx.begin();   
            
            // Get document from data store with given key
            DocumentInfo document = pm.getObjectById(DocumentInfo.class, KeyFactory.stringToKey(documentKey));
            log.fine("Attemping to lock " + document.getTitle() + ".");
            
            // Allow access only if client has document token, and lock time has not expired.
            // Otherwise, throw exception
            Date now = new Date();
            if (document.getLockedBy() != null && document.getLockedBy().equals(userKey) &&
            		document.getLockedUntil() != null && now.before(document.getLockedUntil())) {
            	// Lock document. Use UserValue object to determine document ownership
                lockedDoc = document.lock(userKey);
            } else {
            	throw new LockUnavailable("The document lock does not appear to be yours. If you haven't done so," +
    			" please click Request Doc before locking a document");
            }
            
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        } 
        return lockedDoc;
    }

    /**
	 * Used to retrieve a document in read-only mode.
	 * 
	 * @param documentKey : the key of the document to read
	 * 		  userKey : the key of the user
	 * @return unlockedDoc : UnlockedDocument object which contains the entire document
	 */
    public UnlockedDocument getDocument(String documentKey, String userKey) {
    	// Instantiate JDO-aware application components
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        
        UnlockedDocument unlockedDoc = null;
        try {
        	// Start transaction
            tx.begin();
            
            // Get document with given key and create UnlockedDocument object to return
            DocumentInfo document = pm.getObjectById(DocumentInfo.class, KeyFactory.stringToKey(documentKey));
            unlockedDoc = document.getUnlockedDocumentVersion();
            
            log.fine("Obtaining document: " + document.getTitle() + " for read-only purpose.");
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
        return unlockedDoc;
    }

    /**
	 * Used to save a currently locked document.
	 * 
	 * @param  doc : LockedDocument object returned by lockDocument()
	 * 		   userKey : the key of the user
	 * @return unlockedDoc : the read-only version of the saved document
	 * @throws LockExpired if the locking primitives in the supplied
	 *         LockedDocument object cannot be used to modify the document
	 */
	public UnlockedDocument saveDocument(LockedDocument doc, String userKey)
			throws LockExpired {
		// Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        
        UnlockedDocument unlockedDoc;
        DocumentInfo document;
        try {
        	// Start transaction
            tx.begin();
            
            // If there is no key, assume new document. Otherwise, try to save document
            if (doc.getKey() == null) {
                log.info("Adding document " + doc.getTitle() + " to persistence manager.");
                
            	// Create new document and store object in datastore
            	document = new DocumentInfo(doc.getTitle(), doc.getContents(),
            		doc.getLockedBy(), doc.getLockedUntil());
            	pm.makePersistent(document);
            } else {
            	log.fine("Updating document " + doc.getTitle() + " in persistence manager.");
            	
            	// Mark current date
            	Date now = new Date();
            	
            	// Get document from datastore with given key
            	document = pm.getObjectById(DocumentInfo.class, KeyFactory.stringToKey(doc.getKey()));
            	
            	// If time has not expired, update document contents. Otherwise, throw exception
            	if (document.getLockedBy() != null && document.getLockedBy().equals(userKey) &&
                		document.getLockedUntil() != null && now.before(document.getLockedUntil())) {
            		
            		document.setTitle(doc.getTitle());
            		document.setContents(doc.getContents());
            	} else {
            		throw new LockExpired("Document " + doc.getTitle() + " lock has expired");
            	}
            }
            
            // Create read-only version and unlock document
            unlockedDoc = document.getUnlockedDocumentVersion();
            document.unlock();
           
            // Once a user saved, we now able to inform the next user
            document.removeFirstUser();
            InformNextUserHelper(document);
            
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
		return unlockedDoc;
	}
	
	/**
	 * Inform the next user in the given document's queue that the document is now available
	 * 
	 * @param document : DocumentInfo object containing document itself
	 * @return unlockedDoc : UnlockedDocument object which contains the entire document
	 */
	private static void InformNextUserHelper(DocumentInfo document) {

        // Inform next user of document and update document's new owner
        if (!document.isQueueEmpty()) {
        	String newUser = document.getNextUser();
        	
        	// Set lockedBy field and lockUntil field
        	document.setLockedBy(newUser);
        	Date now = new Date();
        	document.setLockedUntil(new Date(now.getTime() + CollaboratorServiceImpl.EDIT_WINDOW));
        	
        	// Inform user that they are able to edit the document now.
        	DocumentMessage message = new DocumentMessage(newUser, document.getKey(), document.getTitle());
    		PushServer.sendMessage(newUser, message);
        } else {
        	System.out.println("There are no users in this queue");
        	document.setLockedBy(null);
        	document.setLockedBy(null);
        }
	}
	
	/**
	 * Used to release a lock that is no longer needed without saving.
	 * 
	 * @param  doc : the LockedDocument object returned by lockDocument(); any
	 *         modifications made to the document properties in this case are ignored
	 *         userKey : the key of the user
	 * @throws LockExpired if the locking primitives in the supplied
	 *         LockedDocument object cannot be used to release the lock
	 */
	public void releaseLock(LockedDocument doc, String userKey) throws LockExpired {
		// Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		DocumentInfo document;
		try {
			// Start transaction
			tx.begin();
			log.fine("Releasing " + doc.getTitle() + "'s lock.");
			
			Date now = new Date();
			
			// If document already expired, throw exception. Otherwise, unlock document
			if (doc.getLockedBy() != null && doc.getLockedBy().equals(userKey) && 
					doc.getLockedUntil() != null && doc.getLockedUntil().before(now)) {
				
				throw new LockExpired("Lock expired before attempting to release " +
						"document: " + doc.getTitle());
			} else {
				// Get document from datastore with given key
				document = pm.getObjectById(DocumentInfo.class, KeyFactory.stringToKey(doc.getKey()));
				document.unlock();
				
	            // Once a user releases the lock, we are now able to inform the next user
				document.removeFirstUser();
	            InformNextUserHelper(document);
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

	/**
	 * Used to request a document for editing later.
	 * 
	 * @param  documentKey : the key of the document to request
	 * 		   userKey : the key of the user
	 */
	public void request(String documentKey, String userKey) {
		// Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
        	// Start transaction
            tx.begin();
            
            // Get document with given key
            DocumentInfo document = pm.getObjectById(DocumentInfo.class, documentKey);
            log.fine("Requesting document token for: " + document.getTitle());
           
            // Check if user is already in queue. If not, add request to document's queue. 
            // Else, send status message
            if (!(document.containsUser(userKey))) {
	        	document.addUser(userKey);
	
	        	// If current user is next in line, send message, and update document's owner
	        	if (document.getNextUser().equals(userKey)) {
	        		InformNextUserHelper(document);
	        	} else {
	        		// Otherwise, send a status message telling the user to wait for an approximate time
	        		// waitUntil =  Current lockUntil time + EDIT_WINDOW * (number of users ahead of current user who are also waiting)
	        		Date waitUntil = new Date(document.getLockedUntil().getTime() + EDIT_WINDOW * (document.getQueueSize() - 2));
	        		String windowAlertMessage = "Lock for " + document.getTitle() + 
                			" is unavailable. The lock will be released at the latest on " +
                			waitUntil.toString();
	        		StatusMessage message = new StatusMessage(userKey, windowAlertMessage);
	        		PushServer.sendMessage(userKey, message);
	        	}
            } else {
            	// Send status message informing he already requested token for this document.
            	StatusMessage message = new StatusMessage(userKey, "Already requested token, please wait");
        		PushServer.sendMessage(userKey, message);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }		
	}

	/**
	 * Used to remove request a document for editing later.
	 * 
	 * @param  documentKey : the key of the document to remove request
	 * 		   userKey : the key of the user
	 */
	public void cancelRequest(String documentKey, String userKey) {
		// Instantiate JDO-aware application components
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try {
        	// Start transaction
            tx.begin();
            
            // Get document with given key
            DocumentInfo document = pm.getObjectById(DocumentInfo.class, documentKey);
            log.fine("Canceling request for: " + document.getTitle());
            
            // If user currently has request, tell user to lock/save instead. 
            if (document.getLockedBy().equals(userKey)) {
            	StatusMessage message = new StatusMessage(userKey, "You already have the lock. Please lock and save or wait");
	        	PushServer.sendMessage(userKey, message);
			} else if (document.containsUser(userKey)) {
            	// Else remove request from document's queue
            	document.removeUser(userKey);
            	
	        	StatusMessage message = new StatusMessage(userKey, "Successfully removed from queue");
	        	PushServer.sendMessage(userKey, message);
            } else {
            	StatusMessage message = new StatusMessage(userKey, "Did not request this document.");
        		PushServer.sendMessage(userKey, message);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }	
	}

}