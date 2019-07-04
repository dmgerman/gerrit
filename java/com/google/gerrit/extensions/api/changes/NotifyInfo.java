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
DECL|package|com.google.gerrit.extensions.api.changes
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
name|changes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Detailed information about who should be notified about an update. */
end_comment

begin_class
DECL|class|NotifyInfo
specifier|public
class|class
name|NotifyInfo
block|{
DECL|field|accounts
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|accounts
decl_stmt|;
comment|/**    * @param accounts may be either just a list of: account IDs, Full names, usernames, or emails.    *     Also could be a list of those: "Full name<email@example.com>" or "Full name (<ID>)"    */
DECL|method|NotifyInfo (List<String> accounts)
specifier|public
name|NotifyInfo
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|accounts
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
block|}
block|}
end_class

end_unit

