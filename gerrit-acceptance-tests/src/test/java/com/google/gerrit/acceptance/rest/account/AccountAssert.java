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
DECL|package|com.google.gerrit.acceptance.rest.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|account
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|acceptance
operator|.
name|TestAccount
import|;
end_import

begin_class
DECL|class|AccountAssert
specifier|public
class|class
name|AccountAssert
block|{
DECL|method|assertAccountInfo (TestAccount a, AccountInfo ai)
specifier|public
specifier|static
name|void
name|assertAccountInfo
parameter_list|(
name|TestAccount
name|a
parameter_list|,
name|AccountInfo
name|ai
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|a
operator|.
name|id
operator|.
name|get
argument_list|()
operator|==
name|ai
operator|.
name|_account_id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|fullName
argument_list|,
name|ai
operator|.
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|email
argument_list|,
name|ai
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

