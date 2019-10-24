begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
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
name|toMap
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
name|common
operator|.
name|data
operator|.
name|LabelType
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
name|common
operator|.
name|data
operator|.
name|LabelValue
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
name|common
operator|.
name|LabelDefinitionInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestReadView
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|ProjectResource
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|ListLabels
specifier|public
class|class
name|ListLabels
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListLabels (PermissionBackend permissionBackend)
specifier|public
name|ListLabels
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--inherited"
argument_list|,
name|usage
operator|=
literal|"to include inherited label definitions"
argument_list|)
DECL|field|inherited
specifier|private
name|boolean
name|inherited
decl_stmt|;
DECL|method|withInherited (boolean inherited)
specifier|public
name|ListLabels
name|withInherited
parameter_list|(
name|boolean
name|inherited
parameter_list|)
block|{
name|this
operator|.
name|inherited
operator|=
name|inherited
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|LabelDefinitionInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|inherited
condition|)
block|{
name|List
argument_list|<
name|LabelDefinitionInfo
argument_list|>
name|allLabels
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ProjectState
name|projectState
range|:
name|rsrc
operator|.
name|getProjectState
argument_list|()
operator|.
name|treeInOrder
argument_list|()
control|)
block|{
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|READ_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|allLabels
operator|.
name|addAll
argument_list|(
name|listLabels
argument_list|(
name|projectState
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|allLabels
argument_list|)
return|;
block|}
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|rsrc
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|READ_CONFIG
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|listLabels
argument_list|(
name|rsrc
operator|.
name|getProjectState
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|listLabels (ProjectState projectState)
specifier|private
name|List
argument_list|<
name|LabelDefinitionInfo
argument_list|>
name|listLabels
parameter_list|(
name|ProjectState
name|projectState
parameter_list|)
block|{
name|Collection
argument_list|<
name|LabelType
argument_list|>
name|labelTypes
init|=
name|projectState
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|values
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|LabelDefinitionInfo
argument_list|>
name|labels
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|labelTypes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|LabelType
name|labelType
range|:
name|labelTypes
control|)
block|{
name|LabelDefinitionInfo
name|label
init|=
operator|new
name|LabelDefinitionInfo
argument_list|()
decl_stmt|;
name|label
operator|.
name|name
operator|=
name|labelType
operator|.
name|getName
argument_list|()
expr_stmt|;
name|label
operator|.
name|projectName
operator|=
name|projectState
operator|.
name|getName
argument_list|()
expr_stmt|;
name|label
operator|.
name|function
operator|=
name|labelType
operator|.
name|getFunction
argument_list|()
operator|.
name|getFunctionName
argument_list|()
expr_stmt|;
name|label
operator|.
name|values
operator|=
name|labelType
operator|.
name|getValues
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|LabelValue
operator|::
name|formatValue
argument_list|,
name|LabelValue
operator|::
name|getText
argument_list|)
argument_list|)
expr_stmt|;
name|label
operator|.
name|defaultValue
operator|=
name|labelType
operator|.
name|getDefaultValue
argument_list|()
expr_stmt|;
name|label
operator|.
name|branches
operator|=
name|labelType
operator|.
name|getRefPatterns
argument_list|()
operator|!=
literal|null
condition|?
name|labelType
operator|.
name|getRefPatterns
argument_list|()
else|:
literal|null
expr_stmt|;
name|label
operator|.
name|canOverride
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|canOverride
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyAnyScore
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyAnyScore
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyMinScore
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyMinScore
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyMaxScore
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyMaxScore
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyAllScoresIfNoChange
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyAllScoresIfNoChange
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyAllScoresIfNoCodeChange
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyAllScoresIfNoCodeChange
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyAllScoresOnTrivialRebase
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyAllScoresOnTrivialRebase
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|copyAllScoresOnMergeFirstParentUpdate
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|isCopyAllScoresOnMergeFirstParentUpdate
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|allowPostSubmit
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|allowPostSubmit
argument_list|()
argument_list|)
expr_stmt|;
name|label
operator|.
name|ignoreSelfApproval
operator|=
name|toBoolean
argument_list|(
name|labelType
operator|.
name|ignoreSelfApproval
argument_list|()
argument_list|)
expr_stmt|;
name|labels
operator|.
name|add
argument_list|(
name|label
argument_list|)
expr_stmt|;
block|}
name|labels
operator|.
name|sort
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|l
lambda|->
name|l
operator|.
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|labels
return|;
block|}
DECL|method|toBoolean (boolean v)
specifier|private
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|boolean
name|v
parameter_list|)
block|{
return|return
name|v
condition|?
name|v
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

