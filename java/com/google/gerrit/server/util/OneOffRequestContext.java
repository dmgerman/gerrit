begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|gerrit
operator|.
name|server
operator|.
name|InternalUser
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

begin_comment
comment|/**  * Helper to create one-off request contexts.  *  *<p>The user in the request context is {@link InternalUser} or the {@link IdentifiedUser}  * associated to the userId passed as parameter.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|OneOffRequestContext
specifier|public
class|class
name|OneOffRequestContext
block|{
DECL|field|userFactory
specifier|private
specifier|final
name|InternalUser
operator|.
name|Factory
name|userFactory
decl_stmt|;
DECL|field|requestContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|OneOffRequestContext ( InternalUser.Factory userFactory, ThreadLocalRequestContext requestContext, IdentifiedUser.GenericFactory identifiedUserFactory)
name|OneOffRequestContext
parameter_list|(
name|InternalUser
operator|.
name|Factory
name|userFactory
parameter_list|,
name|ThreadLocalRequestContext
name|requestContext
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|)
block|{
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|requestContext
operator|=
name|requestContext
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
block|}
DECL|method|open ()
specifier|public
name|ManualRequestContext
name|open
parameter_list|()
block|{
return|return
operator|new
name|ManualRequestContext
argument_list|(
name|userFactory
operator|.
name|create
argument_list|()
argument_list|,
name|requestContext
argument_list|)
return|;
block|}
DECL|method|openAs (Account.Id userId)
specifier|public
name|ManualRequestContext
name|openAs
parameter_list|(
name|Account
operator|.
name|Id
name|userId
parameter_list|)
block|{
return|return
operator|new
name|ManualRequestContext
argument_list|(
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|userId
argument_list|)
argument_list|,
name|requestContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

