package edu.caltech.cs141b.hw2.gwt.collab.server;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.Query;

import edu.caltech.cs141b.hw2.gwt.collab.client.CollaboratorService;
import edu.caltech.cs141b.hw2.gwt.collab.shared.DocumentMetadata;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockExpired;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockUnavailable;
import edu.caltech.cs141b.hw2.gwt.collab.shared.LockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.shared.UnlockedDocument;
import edu.caltech.cs141b.hw2.gwt.collab.server.PMF;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.apphosting.api.DatastorePb.DatastoreService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CollaboratorServiceImpl extends RemoteServiceServlet implements
		CollaboratorService {
	
	private static final Logger log = Logger.getLogger(CollaboratorServiceImpl.class.toString());
	
	@Override
	public List<DocumentMetadata> getDocumentList() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery();
		
		DocumentJDO[] docs;
		List<DocumentMetadata> docsList;
		try {
			docs = (DocumentJDO[]) ((List<DocumentJDO>) query.execute()).toArray();
			docsList = new ArrayList<DocumentMetadata>();
			for (int i = 0; i < docs.length; i ++) {
				docsList.add(docs[i].getDocumentMetdataObject());
			}
		} finally {
			query.closeAll();
		}

		return docsList;
	}

	@Override
	public LockedDocument lockDocument(String documentKey)
			throws LockUnavailable {
		
		return null;
	}

	@Override
	public UnlockedDocument getDocument(String documentKey) {
		return null;
	}

	@Override
	public UnlockedDocument saveDocument(LockedDocument doc)
			throws LockExpired {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        
        UnlockedDocument unlockedDoc;
        DocumentJDO document;
        try {
            tx.begin();
            if (doc.getKey() == null) {
            	document = new DocumentJDO(doc.getTitle(), doc.getContents(),
            		doc.getLockedBy(), doc.getLockedUntil());
            } else {
            	document = pm.getObjectById(DocumentJDO.class, doc.getKey());
            }
            unlockedDoc = document.unlock();
        	pm.makePersistent(document);

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
		return unlockedDoc;
	}
	
	@Override
	public void releaseLock(LockedDocument doc) throws LockExpired {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		DocumentJDO document;
		try {
			tx.begin();
			Date currentDate = new Date();
			if (doc.getLockedUntil().before(currentDate)) {
				throw new LockExpired("Lock expired before" + doc.getLockedBy() +
						"attempting to release document: " + doc.getTitle());
			} else {
				document = pm.getObjectById(DocumentJDO.class, doc.getKey());
				document.setLockedBy(null);
				document.setLockedUntil(null);
			}
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
		}
	}

}

