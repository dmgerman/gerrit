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
DECL|package|com.google.gerrit.server.mail.receive.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|receive
operator|.
name|data
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
name|server
operator|.
name|mail
operator|.
name|receive
operator|.
name|MailMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/** Base class for all email parsing tests. */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|RawMailMessage
specifier|public
specifier|abstract
class|class
name|RawMailMessage
block|{
comment|// Raw content to feed the parser
DECL|method|raw ()
specifier|public
specifier|abstract
name|String
name|raw
parameter_list|()
function_decl|;
DECL|method|rawChars ()
specifier|public
specifier|abstract
name|int
index|[]
name|rawChars
parameter_list|()
function_decl|;
comment|// Parsed representation for asserting the expected parser output
DECL|method|expectedMailMessage ()
specifier|public
specifier|abstract
name|MailMessage
name|expectedMailMessage
parameter_list|()
function_decl|;
block|}
end_class

end_unit

