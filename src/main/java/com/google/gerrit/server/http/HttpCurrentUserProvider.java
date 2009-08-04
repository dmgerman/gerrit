begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.http
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|http
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
name|client
operator|.
name|reviewdb
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
name|AnonymousUser
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
name|Singleton
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|HttpCurrentUserProvider
class|class
name|HttpCurrentUserProvider
implements|implements
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
block|{
DECL|field|call
specifier|private
specifier|final
name|Provider
argument_list|<
name|GerritCall
argument_list|>
name|call
decl_stmt|;
DECL|field|anonymous
specifier|private
specifier|final
name|AnonymousUser
name|anonymous
decl_stmt|;
DECL|field|identified
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|identified
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpCurrentUserProvider (final Provider<GerritCall> c, final AnonymousUser a, final IdentifiedUser.RequestFactory f)
name|HttpCurrentUserProvider
parameter_list|(
specifier|final
name|Provider
argument_list|<
name|GerritCall
argument_list|>
name|c
parameter_list|,
specifier|final
name|AnonymousUser
name|a
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|f
parameter_list|)
block|{
name|call
operator|=
name|c
expr_stmt|;
name|anonymous
operator|=
name|a
expr_stmt|;
name|identified
operator|=
name|f
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CurrentUser
name|get
parameter_list|()
block|{
specifier|final
name|Account
operator|.
name|Id
name|id
init|=
name|call
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
return|return
name|id
operator|!=
literal|null
condition|?
name|identified
operator|.
name|create
argument_list|(
name|id
argument_list|)
else|:
name|anonymous
return|;
block|}
block|}
end_class

end_unit

