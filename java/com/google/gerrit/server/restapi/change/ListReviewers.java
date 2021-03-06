begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
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
name|change
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
name|entities
operator|.
name|Account
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
name|changes
operator|.
name|ReviewerInfo
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
name|mail
operator|.
name|Address
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
name|ApprovalsUtil
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
name|change
operator|.
name|ChangeResource
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
name|change
operator|.
name|ReviewerJson
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
name|change
operator|.
name|ReviewerResource
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
annotation|@
name|Singleton
DECL|class|ListReviewers
specifier|public
class|class
name|ListReviewers
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ReviewerJson
name|json
decl_stmt|;
DECL|field|resourceFactory
specifier|private
specifier|final
name|ReviewerResource
operator|.
name|Factory
name|resourceFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListReviewers ( ApprovalsUtil approvalsUtil, ReviewerResource.Factory resourceFactory, ReviewerJson json)
name|ListReviewers
parameter_list|(
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|ReviewerResource
operator|.
name|Factory
name|resourceFactory
parameter_list|,
name|ReviewerJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|resourceFactory
operator|=
name|resourceFactory
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|List
argument_list|<
name|ReviewerInfo
argument_list|>
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ReviewerResource
argument_list|>
name|reviewers
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|approvalsUtil
operator|.
name|getReviewers
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
operator|.
name|all
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|reviewers
operator|.
name|containsKey
argument_list|(
name|accountId
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|reviewers
operator|.
name|put
argument_list|(
name|accountId
operator|.
name|toString
argument_list|()
argument_list|,
name|resourceFactory
operator|.
name|create
argument_list|(
name|rsrc
argument_list|,
name|accountId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Address
name|adr
range|:
name|rsrc
operator|.
name|getNotes
argument_list|()
operator|.
name|getReviewersByEmail
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|reviewers
operator|.
name|containsKey
argument_list|(
name|adr
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|reviewers
operator|.
name|put
argument_list|(
name|adr
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|ReviewerResource
argument_list|(
name|rsrc
argument_list|,
name|adr
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|reviewers
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

