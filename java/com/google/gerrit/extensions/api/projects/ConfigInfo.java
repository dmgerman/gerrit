begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.projects
package|package
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
package|;
end_package

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
name|Nullable
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
name|client
operator|.
name|InheritableBoolean
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
name|client
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
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|SubmitType
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
name|ActionInfo
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|ConfigInfo
specifier|public
class|class
name|ConfigInfo
block|{
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|InheritedBooleanInfo
name|useContributorAgreements
decl_stmt|;
DECL|field|useContentMerge
specifier|public
name|InheritedBooleanInfo
name|useContentMerge
decl_stmt|;
DECL|field|useSignedOffBy
specifier|public
name|InheritedBooleanInfo
name|useSignedOffBy
decl_stmt|;
DECL|field|createNewChangeForAllNotInTarget
specifier|public
name|InheritedBooleanInfo
name|createNewChangeForAllNotInTarget
decl_stmt|;
DECL|field|requireChangeId
specifier|public
name|InheritedBooleanInfo
name|requireChangeId
decl_stmt|;
DECL|field|enableSignedPush
specifier|public
name|InheritedBooleanInfo
name|enableSignedPush
decl_stmt|;
DECL|field|requireSignedPush
specifier|public
name|InheritedBooleanInfo
name|requireSignedPush
decl_stmt|;
DECL|field|rejectImplicitMerges
specifier|public
name|InheritedBooleanInfo
name|rejectImplicitMerges
decl_stmt|;
DECL|field|privateByDefault
specifier|public
name|InheritedBooleanInfo
name|privateByDefault
decl_stmt|;
DECL|field|workInProgressByDefault
specifier|public
name|InheritedBooleanInfo
name|workInProgressByDefault
decl_stmt|;
DECL|field|enableReviewerByEmail
specifier|public
name|InheritedBooleanInfo
name|enableReviewerByEmail
decl_stmt|;
DECL|field|matchAuthorToCommitterDate
specifier|public
name|InheritedBooleanInfo
name|matchAuthorToCommitterDate
decl_stmt|;
DECL|field|rejectEmptyCommit
specifier|public
name|InheritedBooleanInfo
name|rejectEmptyCommit
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|public
name|MaxObjectSizeLimitInfo
name|maxObjectSizeLimit
decl_stmt|;
annotation|@
name|Deprecated
comment|// Equivalent to defaultSubmitType.value
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|defaultSubmitType
specifier|public
name|SubmitTypeInfo
name|defaultSubmitType
decl_stmt|;
DECL|field|state
specifier|public
name|ProjectState
name|state
decl_stmt|;
DECL|field|pluginConfig
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigParameterInfo
argument_list|>
argument_list|>
name|pluginConfig
decl_stmt|;
DECL|field|actions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|actions
decl_stmt|;
DECL|field|commentlinks
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CommentLinkInfo
argument_list|>
name|commentlinks
decl_stmt|;
DECL|field|theme
specifier|public
name|ThemeInfo
name|theme
decl_stmt|;
DECL|field|extensionPanelNames
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|extensionPanelNames
decl_stmt|;
DECL|class|InheritedBooleanInfo
specifier|public
specifier|static
class|class
name|InheritedBooleanInfo
block|{
DECL|field|value
specifier|public
name|Boolean
name|value
decl_stmt|;
DECL|field|configuredValue
specifier|public
name|InheritableBoolean
name|configuredValue
decl_stmt|;
DECL|field|inheritedValue
specifier|public
name|Boolean
name|inheritedValue
decl_stmt|;
block|}
DECL|class|MaxObjectSizeLimitInfo
specifier|public
specifier|static
class|class
name|MaxObjectSizeLimitInfo
block|{
comment|/** The effective value in bytes. Null if not set. */
DECL|field|value
annotation|@
name|Nullable
specifier|public
name|String
name|value
decl_stmt|;
comment|/** The value configured explicitly on the project as a formatted string. Null if not set. */
DECL|field|configuredValue
annotation|@
name|Nullable
specifier|public
name|String
name|configuredValue
decl_stmt|;
comment|/**      * Whether the value was inherited or overridden from the project's parent hierarchy or global      * config. Null if not inherited or overridden.      */
DECL|field|summary
annotation|@
name|Nullable
specifier|public
name|String
name|summary
decl_stmt|;
block|}
DECL|class|ConfigParameterInfo
specifier|public
specifier|static
class|class
name|ConfigParameterInfo
block|{
DECL|field|displayName
specifier|public
name|String
name|displayName
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|warning
specifier|public
name|String
name|warning
decl_stmt|;
DECL|field|type
specifier|public
name|ProjectConfigEntryType
name|type
decl_stmt|;
DECL|field|value
specifier|public
name|String
name|value
decl_stmt|;
DECL|field|editable
specifier|public
name|Boolean
name|editable
decl_stmt|;
DECL|field|inheritable
specifier|public
name|Boolean
name|inheritable
decl_stmt|;
DECL|field|configuredValue
specifier|public
name|String
name|configuredValue
decl_stmt|;
DECL|field|inheritedValue
specifier|public
name|String
name|inheritedValue
decl_stmt|;
DECL|field|permittedValues
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|permittedValues
decl_stmt|;
DECL|field|values
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|values
decl_stmt|;
block|}
DECL|class|SubmitTypeInfo
specifier|public
specifier|static
class|class
name|SubmitTypeInfo
block|{
DECL|field|value
specifier|public
name|SubmitType
name|value
decl_stmt|;
DECL|field|configuredValue
specifier|public
name|SubmitType
name|configuredValue
decl_stmt|;
DECL|field|inheritedValue
specifier|public
name|SubmitType
name|inheritedValue
decl_stmt|;
block|}
block|}
end_class

end_unit

