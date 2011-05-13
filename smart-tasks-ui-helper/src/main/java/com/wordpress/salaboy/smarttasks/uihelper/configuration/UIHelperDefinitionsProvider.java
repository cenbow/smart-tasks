/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wordpress.salaboy.smarttasks.uihelper.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.wordpress.salaboy.smarttasks.uihelper.api.TaskOperationsDefinition;
import com.wordpress.salaboy.smarttasks.uihelper.model.GraphTaskOperations;
import com.wordpress.salaboy.smarttasks.uihelper.model.CustomizableDefinition;
import com.wordpress.salaboy.smarttasks.uihelper.model.TaskFormDefinition;
import com.wordpress.salaboy.smarttasks.uihelper.model.TaskListTableDefinition;

/**
 * This class provides definitions for UI Helper.
 * 
 * @author esteban
 */
public class UIHelperDefinitionsProvider {

	public static final String DEFINITIONS_DIRECTORY = "definitions";
	public static final String TASK_LISTS_DEFINITIONS_DIRECTORY = "taskLists";
	public static final String TASK_FORMS_DEFINITIONS_DIRECTORY = "formDefinitions";
	public static final String TASK_OPERATIONS_DIRECTORY = "operationDefinitions";

	private final File definitionsDirectory;

	/**
	 * Creates a new {@link UIHelperDefinitionsProvider} instance. It creates
	 * the {@link File} object with the configuration directory.
	 * 
	 * @param uiHelperConfiguration
	 */
	public UIHelperDefinitionsProvider(
			UIHelperConfiguration uiHelperConfiguration) {
		File uiHelperRootDirectory = uiHelperConfiguration
				.getUiHelperRootDirectory();
		definitionsDirectory = new File(uiHelperRootDirectory,
				DEFINITIONS_DIRECTORY);

		if (!definitionsDirectory.exists()) {
			throw new IllegalStateException(
					"Error creating Definitions Provider: "
							+ definitionsDirectory.getAbsolutePath()
							+ " does not exist!");
		}

		if (!definitionsDirectory.isDirectory()) {
			throw new IllegalStateException(
					"Error creating Definitions Provider: "
							+ definitionsDirectory.getAbsolutePath()
							+ " is not a directory!");
		}
	}

	/**
	 * Returns the definitions for the operations of a task.
	 * @param fileName the file name of the json config file.
	 * @return an instance of {@link TaskOperationsDefinition}.
	 */
	public TaskOperationsDefinition getTaskOperationsDefinition(String fileName) {
		File taskConfigurationFile = new File(new File(
				this.definitionsDirectory, TASK_OPERATIONS_DIRECTORY), fileName
				+ ".config.json");
		TaskOperationsDefinition operationsDefinitions;
		try {
			operationsDefinitions = new GraphTaskOperations(
					taskConfigurationFile);
			return operationsDefinitions;
		} catch (JsonParseException e) {
			Logger.getLogger(UIHelperDefinitionsProvider.class.getName()).log(
					Level.SEVERE,
					"There was a problem getting operations definitions", e);
		} catch (FileNotFoundException e) {
			Logger.getLogger(UIHelperDefinitionsProvider.class.getName()).log(
					Level.SEVERE,
					"There was a problem getting operations definitions", e);
		}
		return null;
	}

	/**
	 * Returns the definitions of the task form.
	 * @param taskType the type of the task.
	 * @param entityId the entity id.
	 * @param profile the profile name.
	 * @return an instance of {@link TaskFormDefinition}.
	 */
	public TaskFormDefinition getTaskFormDefinition(String taskType,
			String entityId, String profile) {
		File taskConfigurationFile = new File(new File(
				this.definitionsDirectory, TASK_FORMS_DEFINITIONS_DIRECTORY),
				taskType + ".config.json");
		if (taskConfigurationFile == null || !taskConfigurationFile.exists()) {
			taskConfigurationFile = new File(
					new File(this.definitionsDirectory,
							TASK_FORMS_DEFINITIONS_DIRECTORY),
					"default.config.json");
		}
		Collection<TaskFormDefinition> definitions = null;
		try {
			Gson gson = new Gson();
			definitions = gson.fromJson(new FileReader(taskConfigurationFile),
					new TypeToken<Collection<TaskFormDefinition>>() {
					}.getType());
			// TODO populate cache here

			// check if a profile is defined for the entityId and taskType
			if (entityId != null && taskType != null) {
				String profileName = entityId + "_" + taskType;
				TaskFormDefinition definition = (TaskFormDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no entityId-taskType profile defined. Lets check for entityId
			// then
			if (entityId != null) {
				String profileName = entityId;
				TaskFormDefinition definition = (TaskFormDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no entityId profile defined. Lets check for taskType then
			if (taskType != null) {
				String profileName = taskType;
				TaskFormDefinition definition = (TaskFormDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no specicial profile foind. Let's find the Default:
			String profileName = TaskFormDefinition.DEFAULT_PROFILE_NAME;
			TaskFormDefinition definition = (TaskFormDefinition) this
					.getTaskTableDefinition(definitions, profileName);

			if (definition != null) {
				return definition;
			}

			// no Default profile -> Error!
			throw new IllegalStateException(
					"No Default configuration defined for : " + taskType);

		} catch (FileNotFoundException ex) {
			throw new IllegalStateException("No configuration defined for : "
					+ taskType, ex);
		}
	}

	/**
	 * Returns the definitions of the task list table.
	 * @param taskListId the id of the task list.
	 * @param entityId the entity id.
	 * @param taskType the type of the task.
	 * @return an instance of {@link TaskListTableDefinition}.
	 */
	public TaskListTableDefinition getTaskListTableDefinition(
			String taskListId, String entityId, String taskType) {
		// TODO: add a cache here

		// Convert the configuration file.
		File taskListConfigurationFile = new File(new File(
				this.definitionsDirectory, TASK_LISTS_DEFINITIONS_DIRECTORY),
				taskListId + ".config.json");
		Collection<TaskListTableDefinition> definitions = null;
		try {
			Gson gson = new Gson();
			definitions = gson.fromJson(new FileReader(
					taskListConfigurationFile),
					new TypeToken<Collection<TaskListTableDefinition>>() {
					}.getType());
			// TODO populate cache here

			// check if a profile is defined for the entityId and taskType
			if (entityId != null && taskType != null) {
				String profileName = entityId + "_" + taskType;
				TaskListTableDefinition definition = (TaskListTableDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no entityId-taskType profile defined. Lets check for entityId
			// then
			if (entityId != null) {
				String profileName = entityId;
				TaskListTableDefinition definition = (TaskListTableDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no entityId profile defined. Lets check for taskType then
			if (taskType != null) {
				String profileName = taskType;
				TaskListTableDefinition definition = (TaskListTableDefinition) this
						.getTaskTableDefinition(definitions, profileName);
				if (definition != null) {
					return definition;
				}
			}

			// no specicial profile foind. Let's find the Default:
			String profileName = TaskFormDefinition.DEFAULT_PROFILE_NAME;
			TaskListTableDefinition definition = (TaskListTableDefinition) this
					.getTaskTableDefinition(definitions, profileName);
			if (definition != null) {
				return definition;
			}

			// no Default profile -> Error!
			throw new IllegalStateException(
					"No Default configuration defined for : " + taskListId);

		} catch (FileNotFoundException ex) {
			throw new IllegalStateException("No configuration defined for : "
					+ taskListId);
		}

	}

	/**
	 * Returns the definition that corresponds to the given profile. If not
	 * found, it returns null.
	 * 
	 * @param definitions
	 * @param profileName
	 * @return
	 */
	private CustomizableDefinition getTaskTableDefinition(
			Collection<? extends CustomizableDefinition> definitions,
			String profileName) {
		for (CustomizableDefinition taskListTableDefinition : definitions) {
			if (taskListTableDefinition.getProfile().equals(profileName)) {
				return taskListTableDefinition;
			}
		}
		return null;
	}

}
