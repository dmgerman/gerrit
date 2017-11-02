begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ConfigValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ProjectConfigEntryType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdatedListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|ProjectConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|ExtensionPoint
DECL|class|ProjectConfigEntry
specifier|public
class|class
name|ProjectConfigEntry
block|{
DECL|field|displayName
specifier|private
specifier|final
name|String
name|displayName
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|inheritable
specifier|private
specifier|final
name|boolean
name|inheritable
decl_stmt|;
DECL|field|defaultValue
specifier|private
specifier|final
name|String
name|defaultValue
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|ProjectConfigEntryType
name|type
decl_stmt|;
DECL|field|permittedValues
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
decl_stmt|;
DECL|method|ProjectConfigEntry (String displayName, String defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, String defaultValue, boolean inheritable)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|inheritable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, String defaultValue, boolean inheritable, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|ProjectConfigEntryType
operator|.
name|STRING
argument_list|,
literal|null
argument_list|,
name|inheritable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, int defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, int defaultValue, boolean inheritable)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|int
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|inheritable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, int defaultValue, boolean inheritable, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|int
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|ProjectConfigEntryType
operator|.
name|INT
argument_list|,
literal|null
argument_list|,
name|inheritable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, long defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|long
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, long defaultValue, boolean inheritable)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|long
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|inheritable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, long defaultValue, boolean inheritable, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|long
name|defaultValue
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|ProjectConfigEntryType
operator|.
name|LONG
argument_list|,
literal|null
argument_list|,
name|inheritable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
comment|// For inheritable boolean use 'LIST' type with InheritableBoolean
DECL|method|ProjectConfigEntry (String displayName, boolean defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|//For inheritable boolean use 'LIST' type with InheritableBoolean
DECL|method|ProjectConfigEntry (String displayName, boolean defaultValue, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|boolean
name|defaultValue
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|ProjectConfigEntryType
operator|.
name|BOOLEAN
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, String defaultValue, List<String> permittedValues)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|permittedValues
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, String defaultValue, List<String> permittedValues, boolean inheritable)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
parameter_list|,
name|boolean
name|inheritable
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|permittedValues
argument_list|,
name|inheritable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, String defaultValue, List<String> permittedValues, boolean inheritable, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|ProjectConfigEntryType
operator|.
name|LIST
argument_list|,
name|permittedValues
argument_list|,
name|inheritable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, T defaultValue, Class<T> permittedValues)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|permittedValues
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|permittedValues
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, T defaultValue, Class<T> permittedValues, boolean inheritable)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|permittedValues
parameter_list|,
name|boolean
name|inheritable
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|permittedValues
argument_list|,
name|inheritable
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, T defaultValue, Class<T> permittedValues, boolean inheritable, String description)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|T
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|permittedValues
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
operator|.
name|name
argument_list|()
argument_list|,
name|ProjectConfigEntryType
operator|.
name|LIST
argument_list|,
name|Arrays
operator|.
name|stream
argument_list|(
name|permittedValues
operator|.
name|getEnumConstants
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|Enum
operator|::
name|name
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|,
name|inheritable
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry ( String displayName, String defaultValue, ProjectConfigEntryType type, List<String> permittedValues, boolean inheritable, String description)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|ProjectConfigEntryType
name|type
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
parameter_list|,
name|boolean
name|inheritable
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|displayName
operator|=
name|displayName
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|permittedValues
operator|=
name|permittedValues
expr_stmt|;
name|this
operator|.
name|inheritable
operator|=
name|inheritable
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
if|if
condition|(
name|type
operator|==
name|ProjectConfigEntryType
operator|.
name|ARRAY
operator|&&
name|inheritable
condition|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"ARRAY doesn't support inheritable values"
argument_list|)
throw|;
block|}
block|}
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|isInheritable ()
specifier|public
name|boolean
name|isInheritable
parameter_list|()
block|{
return|return
name|inheritable
return|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|getType ()
specifier|public
name|ProjectConfigEntryType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getPermittedValues ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPermittedValues
parameter_list|()
block|{
return|return
name|permittedValues
return|;
block|}
comment|/**    * @param project project state.    * @return whether the project is editable.    */
DECL|method|isEditable (ProjectState project)
specifier|public
name|boolean
name|isEditable
parameter_list|(
name|ProjectState
name|project
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**    * @param project project state.    * @return any warning associated with the project.    */
DECL|method|getWarning (ProjectState project)
specifier|public
name|String
name|getWarning
parameter_list|(
name|ProjectState
name|project
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/**    * Called before the project config is updated. To modify the value before the project config is    * updated, override this method and return the modified value. Default implementation returns the    * same value.    *    * @param configValue the original configValue that was entered.    * @return the modified configValue.    */
DECL|method|preUpdate (ConfigValue configValue)
specifier|public
name|ConfigValue
name|preUpdate
parameter_list|(
name|ConfigValue
name|configValue
parameter_list|)
block|{
return|return
name|configValue
return|;
block|}
comment|/**    * Called after reading the project config value. To modify the value before returning it to the    * client, override this method and return the modified value. Default implementation returns the    * same value.    *    * @param project the project.    * @param value the actual value of the config entry (computed out of the configured value, the    *     inherited value and the default value).    * @return the modified value.    */
DECL|method|onRead (ProjectState project, String value)
specifier|public
name|String
name|onRead
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|value
return|;
block|}
comment|/**    * Called after reading the project config value of type ARRAY. To modify the values before    * returning it to the client, override this method and return the modified values. Default    * implementation returns the same values.    *    * @param project the project.    * @param values the actual values of the config entry (computed out of the configured value, the    *     inherited value and the default value).    * @return the modified values.    */
DECL|method|onRead (ProjectState project, List<String> values)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|onRead
parameter_list|(
name|ProjectState
name|project
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
return|;
block|}
comment|/**    * Called after a project config is updated.    *    * @param project project name.    * @param oldValue old entry value.    * @param newValue new entry value.    */
DECL|method|onUpdate (Project.NameKey project, String oldValue, String newValue)
specifier|public
name|void
name|onUpdate
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|oldValue
parameter_list|,
name|String
name|newValue
parameter_list|)
block|{}
comment|/**    * Called after a project config is updated.    *    * @param project project name.    * @param oldValue old entry value.    * @param newValue new entry value.    */
DECL|method|onUpdate (Project.NameKey project, Boolean oldValue, Boolean newValue)
specifier|public
name|void
name|onUpdate
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Boolean
name|oldValue
parameter_list|,
name|Boolean
name|newValue
parameter_list|)
block|{}
comment|/**    * Called after a project config is updated.    *    * @param project project name.    * @param oldValue old entry value.    * @param newValue new entry value.    */
DECL|method|onUpdate (Project.NameKey project, Integer oldValue, Integer newValue)
specifier|public
name|void
name|onUpdate
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Integer
name|oldValue
parameter_list|,
name|Integer
name|newValue
parameter_list|)
block|{}
comment|/**    * Called after a project config is updated.    *    * @param project project name.    * @param oldValue old entry value.    * @param newValue new entry value.    */
DECL|method|onUpdate (Project.NameKey project, Long oldValue, Long newValue)
specifier|public
name|void
name|onUpdate
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Long
name|oldValue
parameter_list|,
name|Long
name|newValue
parameter_list|)
block|{}
DECL|class|UpdateChecker
specifier|public
specifier|static
class|class
name|UpdateChecker
implements|implements
name|GitReferenceUpdatedListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UpdateChecker
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|pluginConfigEntries
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|pluginConfigEntries
decl_stmt|;
annotation|@
name|Inject
DECL|method|UpdateChecker ( GitRepositoryManager repoManager, DynamicMap<ProjectConfigEntry> pluginConfigEntries)
name|UpdateChecker
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|DynamicMap
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|pluginConfigEntries
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|pluginConfigEntries
operator|=
name|pluginConfigEntries
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onGitReferenceUpdated (Event event)
specifier|public
name|void
name|onGitReferenceUpdated
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|Project
operator|.
name|NameKey
name|p
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|event
operator|.
name|getProjectName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|event
operator|.
name|getRefName
argument_list|()
operator|.
name|equals
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
condition|)
block|{
return|return;
block|}
try|try
block|{
name|ProjectConfig
name|oldCfg
init|=
name|parseConfig
argument_list|(
name|p
argument_list|,
name|event
operator|.
name|getOldObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|ProjectConfig
name|newCfg
init|=
name|parseConfig
argument_list|(
name|p
argument_list|,
name|event
operator|.
name|getNewObjectId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldCfg
operator|!=
literal|null
operator|&&
name|newCfg
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|e
range|:
name|pluginConfigEntries
control|)
block|{
name|ProjectConfigEntry
name|configEntry
init|=
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|newValue
init|=
name|getValue
argument_list|(
name|newCfg
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|String
name|oldValue
init|=
name|getValue
argument_list|(
name|oldCfg
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|newValue
operator|==
literal|null
operator|&&
name|oldValue
operator|==
literal|null
operator|)
operator|||
operator|(
name|newValue
operator|!=
literal|null
operator|&&
name|newValue
operator|.
name|equals
argument_list|(
name|oldValue
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
switch|switch
condition|(
name|configEntry
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|BOOLEAN
case|:
name|configEntry
operator|.
name|onUpdate
argument_list|(
name|p
argument_list|,
name|toBoolean
argument_list|(
name|oldValue
argument_list|)
argument_list|,
name|toBoolean
argument_list|(
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|INT
case|:
name|configEntry
operator|.
name|onUpdate
argument_list|(
name|p
argument_list|,
name|toInt
argument_list|(
name|oldValue
argument_list|)
argument_list|,
name|toInt
argument_list|(
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|LONG
case|:
name|configEntry
operator|.
name|onUpdate
argument_list|(
name|p
argument_list|,
name|toLong
argument_list|(
name|oldValue
argument_list|)
argument_list|,
name|toLong
argument_list|(
name|newValue
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|LIST
case|:
case|case
name|STRING
case|:
case|case
name|ARRAY
case|:
default|default:
name|configEntry
operator|.
name|onUpdate
argument_list|(
name|p
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to check if plugin config of project %s was updated."
argument_list|,
name|p
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|parseConfig (Project.NameKey p, String idStr)
specifier|private
name|ProjectConfig
name|parseConfig
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
name|String
name|idStr
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|RepositoryNotFoundException
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|idStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|p
argument_list|)
init|)
block|{
name|ProjectConfig
name|pc
init|=
operator|new
name|ProjectConfig
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|pc
operator|.
name|load
argument_list|(
name|repo
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|pc
return|;
block|}
block|}
DECL|method|getValue (ProjectConfig cfg, Entry<ProjectConfigEntry> e)
specifier|private
specifier|static
name|String
name|getValue
parameter_list|(
name|ProjectConfig
name|cfg
parameter_list|,
name|Entry
argument_list|<
name|ProjectConfigEntry
argument_list|>
name|e
parameter_list|)
block|{
name|String
name|value
init|=
name|cfg
operator|.
name|getPluginConfig
argument_list|(
name|e
operator|.
name|getPluginName
argument_list|()
argument_list|)
operator|.
name|getString
argument_list|(
name|e
operator|.
name|getExportName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getDefaultValue
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
DECL|method|toBoolean (String value)
specifier|private
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|toInt (String value)
specifier|private
specifier|static
name|Integer
name|toInt
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|toLong (String value)
specifier|private
specifier|static
name|Long
name|toLong
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit
