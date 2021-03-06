/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.smarttasks.activiti5wrapper;

import com.wordpress.salaboy.api.HumanTaskServiceOperations;
import com.wordpress.salaboy.api.ServiceLifeCycleManager;
import com.wordpress.salaboy.smarttasks.activiti5wrapper.adapter.Activiti5TTaskAbstractAdapter;
import com.wordpress.salaboy.smarttasks.activiti5wrapper.adapter.Activiti5TTaskAdapter;
import com.wordpress.salaboy.smarttasks.activiti5wrapper.conf.ActivitiHumanTaskClientConfiguration;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.task.Task;
import org.example.ws_ht.TOrganizationalEntity;
import org.example.ws_ht.api.TAttachment;
import org.example.ws_ht.api.TAttachmentInfo;
import org.example.ws_ht.api.TComment;
import org.example.ws_ht.api.TStatus;
import org.example.ws_ht.api.TTask;
import org.example.ws_ht.api.TTaskAbstract;
import org.example.ws_ht.api.TTaskQueryResultSet;
import org.example.ws_ht.api.wsdl.IllegalAccessFault;
import org.example.ws_ht.api.wsdl.IllegalArgumentFault;
import org.example.ws_ht.api.wsdl.IllegalOperationFault;
import org.example.ws_ht.api.wsdl.IllegalStateFault;
import org.example.ws_ht.api.wsdl.RecipientNotAllowed;
import org.example.ws_ht.api.xsd.TTime;

/**
 * 
 * @author salaboy
 */
public class ActivitiHumanTaskServiceOperations implements
		HumanTaskServiceOperations {

	private ActivitiHumanTaskClientConfiguration config;
	private String authorizedEntityId;
	private String locale;
	private TaskService taskService;
	private FormService formService;
	private HistoryService historyService;

	/**
	 * Indicates whether the service has been initialized
	 */
	private boolean initialized = false;

	public ActivitiHumanTaskServiceOperations(
			ActivitiHumanTaskClientConfiguration config) {
		this.config = config;
		ProcessEngine processEngine = ProcessEngines
		.getDefaultProcessEngine();
		if (processEngine == null) {
			this.initialized = false;
		}
		else {
			this.taskService = processEngine.getTaskService();
			this.formService = processEngine.getFormService();
			this.historyService = processEngine.getHistoryService();
			this.initialized = true;
		}
	}

	private boolean initializeIfNeeded() {
		//It is not a great solution, but it makes it work if activiti gets down and up...
		try {
			if (!this.initialized) {
				ClassLoader classLoader = ReflectUtil.getClassLoader();
				ProcessEngines
						.retry("file://" + classLoader.getResource("activiti.cfg.xml").getFile());
				ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
				if (processEngine == null) {
					Logger.getLogger(getClass().getName())
							.log(Level.WARNING,
									"There was an error starting activiti service!");
					this.initialized = false;
				} else {
					this.taskService = processEngine.getTaskService();
					this.formService = processEngine.getFormService();
					this.historyService = processEngine.getHistoryService();
					this.initialized = true;
				}
			}
			return this.initialized;
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
					"There was an error starting activiti service!", ex);
			this.initialized = false;
		}
		return this.initialized;
	}

	public void nominate(String identifier,
			TOrganizationalEntity organizationalEntity)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void getFault(String identifier, Holder<String> faultName,
			Holder<Object> faultData) throws IllegalArgumentFault,
			IllegalStateFault, IllegalOperationFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void forward(String identifier,
			TOrganizationalEntity organizationalEntity)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void deleteOutput(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<QName> getRenderingTypes(Object identifier)
			throws IllegalArgumentFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<TComment> getComments(String identifier)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addAttachment(String identifier, String name,
			String accessType, Object attachment) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void activate(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setOutput(String identifier, String part, Object taskData)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void start(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		// Do Nothing -> Activiti doesn't have start task method

	}

	public TTaskQueryResultSet query(String selectClause, String whereClause,
			String orderByClause, Integer maxTasks, Integer taskIndexOffset)
			throws IllegalArgumentFault, IllegalStateFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void deleteAttachments(String identifier, String attachmentName)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<TTaskAbstract> getMyTaskAbstracts(String taskType,
			String genericHumanRole, String workQueue, List<TStatus> status,
			String whereClause, String orderByClause, String createdOnClause,
			Integer maxTasks, Integer fromTaskNumber)
			throws IllegalArgumentFault, IllegalStateFault {
		if (!this.initializeIfNeeded()) {
			return Collections.emptyList();
		}
		List<Task> tasks = taskService.createTaskQuery()
				.taskAssignee(genericHumanRole).list();
		return Activiti5TTaskAbstractAdapter.getInstance().adaptCollection(
				tasks);
	}

	public void skip(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalOperationFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<TAttachment> getAttachments(String identifier,
			String attachmentName) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		if (!this.initializeIfNeeded()) {
			throw new IllegalStateException(
					"Could not connect to activiti service.");
		}
		FormData formData = formService.getTaskFormData(identifier);
		List<FormProperty> properties = formData.getFormProperties();

		List<TAttachment> attachs = new ArrayList<TAttachment>();

		for (FormProperty property : properties) {
			if (property.getName().equals(attachmentName)) {
				TAttachment attach = new TAttachment();
				TAttachmentInfo attachInfo = new TAttachmentInfo();
				attachInfo.setName(property.getName());
				attachInfo.setContentType(property.getType().getName());
				attach.setAttachmentInfo(attachInfo);
				attach.setValue(property.getValue());
				attachs.add(attach);
			}
		}

		return attachs;

	}

	public String getTaskDescription(String identifier, String contentType)
			throws IllegalArgumentFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void release(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public TTask getTaskInfo(String identifier) throws IllegalArgumentFault {
		if (!this.initializeIfNeeded()) {
			throw new IllegalStateException(
					"Could not connect to activiti service.");
		}
		Task task = taskService.createTaskQuery().taskId(identifier)
				.singleResult();
		if (task == null) {
			return null;
		}
		return Activiti5TTaskAdapter.getInstance().adapt(task);
	}

	public void remove(String identifier) throws IllegalArgumentFault,
			IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void suspend(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<TTask> getMyTasks(String taskType, String genericHumanRole,
			String workQueue, List<TStatus> status, String whereClause,
			String orderByClause, String createdOnClause, Integer maxTasks,
			Integer fromTaskNumber) throws IllegalArgumentFault,
			IllegalStateFault {
		if (!this.initializeIfNeeded()) {
			return Collections.emptyList();
		}
		List<Task> tasks = taskService.createTaskQuery()
				.taskAssignee(genericHumanRole).list();
		return Activiti5TTaskAdapter.getInstance().adaptCollection(tasks);
	}

	public void setGenericHumanRole(String identifier, String genericHumanRole,
			TOrganizationalEntity organizationalEntity)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object getInput(String identifier, String part)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object getRendering(String identifier, QName renderingType)
			throws IllegalArgumentFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setFault(String identifier, String faultName, Object faultData)
			throws IllegalArgumentFault, IllegalStateFault,
			IllegalOperationFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void delegate(String identifier,
			TOrganizationalEntity organizationalEntity)
			throws RecipientNotAllowed, IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void stop(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object getOutput(String identifier, String part)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<TAttachmentInfo> getAttachmentInfos(String identifier)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		FormData formData = formService.getTaskFormData(identifier);
		List<FormProperty> properties = formData.getFormProperties();

		List<TAttachmentInfo> attachInfos = new ArrayList<TAttachmentInfo>();

		for (FormProperty property : properties) {

			TAttachmentInfo attachInfo = new TAttachmentInfo();
			attachInfo.setName(property.getName());
			attachInfo.setContentType(property.getType().getName());

			attachInfos.add(attachInfo);
		}

		return attachInfos;
	}

	public void suspendUntil(String identifier, TTime time)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void addComment(String identifier, String text)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void complete(String identifier, Object taskData)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		formService.submitTaskFormData(identifier,
				(Map<String, String>) taskData);
	}

	public void setPriority(String identifier, BigInteger priority)
			throws IllegalArgumentFault, IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void resume(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void claim(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		if (!this.initializeIfNeeded()) {
			throw new IllegalStateException(
					"Could not connect to activiti service.");
		}
		taskService.claim(identifier, this.authorizedEntityId);
	}

	public void fail(String identifier, String faultName, Object faultData)
			throws IllegalArgumentFault, IllegalStateFault,
			IllegalOperationFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void deleteFault(String identifier) throws IllegalArgumentFault,
			IllegalStateFault, IllegalAccessFault {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setAuthorizedEntityId(String entityId) {
		this.authorizedEntityId = entityId;
	}

	public String getAuthorizedEntityId() {
		return this.authorizedEntityId;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void initializeService() {
		// FIX ME: This method should not be here!!!!!

	}

	public void cleanUpService() {
		// FIX ME: This method should not be here!!!!!

	}

	public void setServiceLifeCycle(String name,
			ServiceLifeCycleManager serviceLifeCycle) {
		// FIX ME: This method should not be here!!!!! there is no managed
		// services here..
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getTaskOriginatorType(String taskId) {
		return ActivitiHumanTaskClientConfiguration.TYPE;
	}

}
