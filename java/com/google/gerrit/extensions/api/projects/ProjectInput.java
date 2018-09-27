begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|SubmitType
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
DECL|class|ProjectInput
specifier|public
class|class
name|ProjectInput
block|{
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|parent
specifier|public
name|String
name|parent
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|permissionsOnly
specifier|public
name|boolean
name|permissionsOnly
decl_stmt|;
DECL|field|createEmptyCommit
specifier|public
name|boolean
name|createEmptyCommit
decl_stmt|;
DECL|field|submitType
specifier|public
name|SubmitType
name|submitType
decl_stmt|;
DECL|field|branches
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|owners
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|owners
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|InheritableBoolean
name|useContributorAgreements
decl_stmt|;
DECL|field|useSignedOffBy
specifier|public
name|InheritableBoolean
name|useSignedOffBy
decl_stmt|;
DECL|field|useContentMerge
specifier|public
name|InheritableBoolean
name|useContentMerge
decl_stmt|;
DECL|field|requireChangeId
specifier|public
name|InheritableBoolean
name|requireChangeId
decl_stmt|;
DECL|field|createNewChangeForAllNotInTarget
specifier|public
name|InheritableBoolean
name|createNewChangeForAllNotInTarget
decl_stmt|;
DECL|field|rejectEmptyCommit
specifier|public
name|InheritableBoolean
name|rejectEmptyCommit
decl_stmt|;
DECL|field|enableSignedPush
specifier|public
name|InheritableBoolean
name|enableSignedPush
decl_stmt|;
DECL|field|requireSignedPush
specifier|public
name|InheritableBoolean
name|requireSignedPush
decl_stmt|;
DECL|field|maxObjectSizeLimit
specifier|public
name|String
name|maxObjectSizeLimit
decl_stmt|;
DECL|field|pluginConfigValues
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ConfigValue
argument_list|>
argument_list|>
name|pluginConfigValues
decl_stmt|;
block|}
end_class

end_unit

