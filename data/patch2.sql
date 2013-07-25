ALTER TABLE customer ADD COLUMN relatedRoyalty FLOAT;
ALTER TABLE customer CHANGE royalty authorRoyalty FLOAT;

UPDATE customer SET relatedRoyalty=authorRoyalty;


ALTER TABLE customer_report_item CHANGE price price FLOAT;