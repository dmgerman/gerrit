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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|CurrentUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|Provider
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
name|util
operator|.
name|Providers
import|;
end_import

begin_comment
comment|/** Closeable version of a {@link RequestContext} with manually-specified providers. */
end_comment

begin_class
DECL|class|ManualRequestContext
specifier|public
class|class
name|ManualRequestContext
implements|implements
name|RequestContext
implements|,
name|AutoCloseable
block|{
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|requestContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|old
specifier|private
specifier|final
name|RequestContext
name|old
decl_stmt|;
DECL|method|ManualRequestContext ( CurrentUser user, SchemaFactory<ReviewDb> schemaFactory, ThreadLocalRequestContext requestContext)
specifier|public
name|ManualRequestContext
parameter_list|(
name|CurrentUser
name|user
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|ThreadLocalRequestContext
name|requestContext
parameter_list|)
throws|throws
name|OrmException
block|{
name|this
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
name|user
argument_list|)
argument_list|,
name|schemaFactory
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
block|}
DECL|method|ManualRequestContext ( Provider<CurrentUser> userProvider, SchemaFactory<ReviewDb> schemaFactory, ThreadLocalRequestContext requestContext)
specifier|public
name|ManualRequestContext
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|ThreadLocalRequestContext
name|requestContext
parameter_list|)
throws|throws
name|OrmException
block|{
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|Providers
operator|.
name|of
argument_list|(
name|schemaFactory
operator|.
name|open
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|requestContext
operator|=
name|requestContext
expr_stmt|;
name|old
operator|=
name|requestContext
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|userProvider
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|requestContext
operator|.
name|setContext
argument_list|(
name|old
argument_list|)
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

