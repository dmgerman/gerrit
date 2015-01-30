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
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|HashMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|SetMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|account
operator|.
name|AccountByEmailCache
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Fake implementation of {@link AccountByEmailCache} for testing. */
end_comment

begin_class
DECL|class|FakeAccountByEmailCache
specifier|public
class|class
name|FakeAccountByEmailCache
implements|implements
name|AccountByEmailCache
block|{
DECL|field|byEmail
specifier|private
specifier|final
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|byEmail
decl_stmt|;
DECL|field|anyEmail
specifier|private
specifier|final
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|anyEmail
decl_stmt|;
DECL|method|FakeAccountByEmailCache ()
specifier|public
name|FakeAccountByEmailCache
parameter_list|()
block|{
name|byEmail
operator|=
name|HashMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
name|anyEmail
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (String email)
specifier|public
specifier|synchronized
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|get
parameter_list|(
name|String
name|email
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|Sets
operator|.
name|union
argument_list|(
name|byEmail
operator|.
name|get
argument_list|(
name|email
argument_list|)
argument_list|,
name|anyEmail
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|evict (String email)
specifier|public
specifier|synchronized
name|void
name|evict
parameter_list|(
name|String
name|email
parameter_list|)
block|{
comment|// Do nothing.
block|}
DECL|method|put (String email, Account.Id id)
specifier|public
specifier|synchronized
name|void
name|put
parameter_list|(
name|String
name|email
parameter_list|,
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|byEmail
operator|.
name|put
argument_list|(
name|email
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|putAny (Account.Id id)
specifier|public
specifier|synchronized
name|void
name|putAny
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|anyEmail
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

