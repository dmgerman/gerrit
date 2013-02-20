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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|server
operator|.
name|IdentifiedUser
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
name|TypeLiteral
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|AssistedInject
import|;
end_import

begin_class
DECL|class|ReviewerResource
specifier|public
class|class
name|ReviewerResource
extends|extends
name|ChangeResource
block|{
DECL|field|REVIEWER_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ReviewerResource
argument_list|>
argument_list|>
name|REVIEWER_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|ReviewerResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|interface|Factory
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (ChangeResource rsrc, IdentifiedUser user)
name|ReviewerResource
name|create
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|)
function_decl|;
DECL|method|create (ChangeResource rsrc, Account account)
name|ReviewerResource
name|create
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|Account
name|account
parameter_list|)
function_decl|;
block|}
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ReviewerResource (@ssisted ChangeResource rsrc, @Assisted IdentifiedUser user)
name|ReviewerResource
parameter_list|(
annotation|@
name|Assisted
name|ChangeResource
name|rsrc
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|super
argument_list|(
name|rsrc
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|ReviewerResource (IdentifiedUser.GenericFactory userFactory, @Assisted ChangeResource rsrc, @Assisted Account account)
name|ReviewerResource
parameter_list|(
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
annotation|@
name|Assisted
name|ChangeResource
name|rsrc
parameter_list|,
annotation|@
name|Assisted
name|Account
name|account
parameter_list|)
block|{
name|this
argument_list|(
name|rsrc
argument_list|,
name|userFactory
operator|.
name|create
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
block|}
end_class

end_unit

