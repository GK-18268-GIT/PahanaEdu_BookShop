package com.system.controller;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.system.db.ManageItemDao;
import com.system.model.ManageItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,    
    maxFileSize = 1024 * 1024 * 10,     
    maxRequestSize = 1024 * 1024 * 50   
)
public class ManageItemServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String Upload_Path = "/resources/itemUploads";
    private ManageItemDao manageItemDao;

    @Override
    public void init() {
    	manageItemDao  = new ManageItemDao();
        if(manageItemDao != null) {
        	System.out.println("[DEBUG] ManageItemDao initialized: " + (manageItemDao !=null));
        } else {
        	System.out.println("[DEBUG] ManageItemDao initialized failed!");
        }
       
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = request.getParameter("action");
    	
    	if(action == null) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
    		return;
    	} 
    	
    	try {
    		switch(action) {
    			case "edit" :
    				handleUpdate(request, response);
    				break;
    			case "showAddForm" :
    				request.getRequestDispatcher("/WEB-INF/admin/addItems.jsp").forward(request, response);
    				break;
    			case "itemList" :
    				showListItem(request, response);
    				break;
    			case "inStock":
    				showInStockItems(request, response);
    				break;
    			default:
    				showListItem(request, response);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("[Error] An error occured while processing. please try again!");
    		request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    	}	
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String action = null;
    	Map<String, String> formData = new HashMap<>();
    	String fileName = null;
    	
    	if(ServletFileUpload.isMultipartContent(request)) {
    		try {
    			fileName = handleFileUploads(request, response, formData);
    			action = formData.get("action");
    		} catch(Exception e) {
    			e.printStackTrace();
    			handleError(request, response, "Missing action!");
    		}
    	} else {
    		action = request.getParameter("action");
    	}
    	
    	if(action == null || action.isEmpty()) {
    		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
    		return;
    	} 
    	
    	try {
    		switch(action) {
    			case "add" :
    				addNewItems(request, response, formData, fileName);
    				break;
    			case "update" :
    				updateItems(request, response, formData, fileName);
    				break;
    			case "delete" :
    				deleteItems(request, response);
    				break;
    			case "itemList" :
    				showListItem(request, response);
    				break;
    			default :
    				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action!");
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("[Error] An error occured while processing. please try again!");
    		request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    	}
    	
    }
    
    private String handleFileUploads(HttpServletRequest request, HttpServletResponse response, Map<String, String> formData) throws Exception {
    	if (!ServletFileUpload.isMultipartContent(request)) return "";

        String filePath = request.getServletContext().getRealPath("/");
        String uploadPath = filePath + "resources" + File.separator + "itemUploads";
        
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // Parse multipart request
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<FileItem> items = upload.parseRequest(request);
        String fileName = "";

        for (FileItem item : items) {
            if (item.isFormField()) {
                formData.put(item.getFieldName(), item.getString("UTF-8"));
            } else if (item.getSize() > 0) {
                String originalName = new File(item.getName()).getName();
                String esc = originalName.substring(originalName.lastIndexOf("."));
                fileName = UUID.randomUUID() + esc;
                item.write(new File(uploadPath + File.separator + fileName));
            }
        }
        return fileName;
    }
    
    private void itemFormData(ManageItem item, Map<String, String> data) {
    	item.setItemName(data.get("itemName"));
    	item.setItemDescription(data.get("itemDescription"));
    	item.setCategory(data.get("category"));
    	
    	String unitPriceStr = data.get("unitPrice");   	
    	try {
    		item.setUnitPrice((unitPriceStr != null && !unitPriceStr.isEmpty()) ? Double.parseDouble(unitPriceStr) : 0.0);
    	} catch(NumberFormatException e) {
    		item.setUnitPrice(0.0);
    	}
    	
    	String stockQtyStr = data.get("stockQty");
    	try {
    		item.setStockQty((stockQtyStr != null && !stockQtyStr.isEmpty()) ? Integer.parseInt(stockQtyStr) : 0);
    	} catch(NumberFormatException e) {
    		item.setStockQty(0);
    	}
    	item.setStatus(data.get("status"));
    	
    }
    
    private void deleteOldImage(String fileName, HttpServletRequest request, HttpServletResponse response) {
    	if (fileName == null || fileName.isEmpty()) return;
        
        String filePath = request.getServletContext().getRealPath("/");
        File imageFile = new File(filePath + "resources" + File.separator + "itemUploads" + File.separator + fileName);
        if (imageFile.exists()) imageFile.delete();
    	
    }
    
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String itemId = request.getParameter("itemId");
    	if(itemId == null || itemId.isEmpty()) {
    		throw new ServletException("Missing Id");
    	}
    	
    	ManageItem item = manageItemDao.getItemById(itemId);
    	if(item == null) {
    		throw new ServletException("Item not found! " + itemId);
    	}
    	
    	request.setAttribute("item", item);
    	request.getRequestDispatcher("/WEB-INF/admin/updateItems.jsp").forward(request, response);
    }

    private void addNewItems(HttpServletRequest request, HttpServletResponse response, Map<String, String> formData, String fileName) throws Exception {

        ManageItem item = new ManageItem();
        itemFormData(item, formData);
        item.setItemImage(fileName);
        item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        
        manageItemDao.addNewItem(item);
        response.sendRedirect("ManageItemServlet?action=itemList&success=Item+added+successfully");
        
    }

    private void updateItems(HttpServletRequest request, HttpServletResponse response, Map<String, String> formData, String newFileName) throws Exception {
    	
    	String itemId = formData.get("itemId");
    	if(itemId == null || itemId.isEmpty()) {
    		throw new ServletException("Missing Id");
    	}
    	
    	ManageItem addedItem = manageItemDao.getItemById(itemId);
    	if(addedItem == null) {
    		throw new ServletException("Item not found! " + itemId);
    	}
    	
    	itemFormData(addedItem, formData);
    	addedItem.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    	
    	if(newFileName !=null && !newFileName.isEmpty()) {
    		deleteOldImage(addedItem.getItemImage(), request, response);
    		addedItem.setItemImage(newFileName);
    	}
    	
    	if(manageItemDao.updateItem(addedItem)) {
    		response.sendRedirect("ManageItemServlet?action=itemList&success=Item+update+successfully");
    	} else {
    		throw new ServletException("Failde to update item");
    	}
    	
    	
      }

	private void deleteItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String itemId = request.getParameter("itemId");
        if(itemId == null || itemId.isEmpty()) {
            handleError(request, response, "Missing item ID!");
            return;
        }
        
        ManageItem item = manageItemDao.getItemById(itemId);
        if(item == null) {
        	handleError(request, response, "Item not found!");
        	return;
        }
        
        if(item.getItemImage() != null) {
        	deleteOldImage(item.getItemImage(), request, response); 	
        }
        
        manageItemDao.deleteItem(itemId);   
        response.sendRedirect("ManageItemServlet?action=itemList&success=Item+delete+successfully");
    }

     private void showListItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	 	List<ManageItem> itemList = manageItemDao.getAllItems();
            request.setAttribute("items", itemList);
            request.getRequestDispatcher("/WEB-INF/admin/listItems.jsp").forward(request, response);
        }
    		     
	private void showInStockItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	 
    	 try {
    		 List<ManageItem> inStockItems = manageItemDao.getInStockItems();
    		 request.setAttribute("itemList", inStockItems);
    		 
    		 request.getRequestDispatcher("/WEB-INF/items/displayItems.jsp").forward(request, response);
    		 return;		 
    	 } catch(SQLException e) {
    		 e.printStackTrace();
    		 System.out.println("[Error]: Failed to retrieve item!");
    	 }
    	 
     }

     private void handleError(HttpServletRequest request, HttpServletResponse response, String message) 
    		 throws ServletException, IOException {
    	 request.setAttribute("errorMessage", message);
    	 request.getRequestDispatcher("/WEB-INF/admin/error.jsp").forward(request, response);
    	 return;
     }
}