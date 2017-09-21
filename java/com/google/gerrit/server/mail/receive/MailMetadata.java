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
DECL|package|com.google.gerrit.server.mail.receive
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
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** MailMetadata represents metadata parsed from inbound email. */
end_comment

begin_class
DECL|class|MailMetadata
specifier|public
class|class
name|MailMetadata
block|{
DECL|field|changeNumber
specifier|public
name|Integer
name|changeNumber
decl_stmt|;
DECL|field|patchSet
specifier|public
name|Integer
name|patchSet
decl_stmt|;
DECL|field|author
specifier|public
name|String
name|author
decl_stmt|;
comment|// Author of the email
DECL|field|timestamp
specifier|public
name|Timestamp
name|timestamp
decl_stmt|;
DECL|field|messageType
specifier|public
name|String
name|messageType
decl_stmt|;
comment|// we expect comment here
DECL|method|hasRequiredFields ()
specifier|public
name|boolean
name|hasRequiredFields
parameter_list|()
block|{
return|return
name|changeNumber
operator|!=
literal|null
operator|&&
name|patchSet
operator|!=
literal|null
operator|&&
name|author
operator|!=
literal|null
operator|&&
name|timestamp
operator|!=
literal|null
operator|&&
name|messageType
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"Change-Number"
argument_list|,
name|changeNumber
argument_list|)
operator|.
name|add
argument_list|(
literal|"Patch-Set"
argument_list|,
name|patchSet
argument_list|)
operator|.
name|add
argument_list|(
literal|"Author"
argument_list|,
name|author
argument_list|)
operator|.
name|add
argument_list|(
literal|"Timestamp"
argument_list|,
name|timestamp
argument_list|)
operator|.
name|add
argument_list|(
literal|"Message-Type"
argument_list|,
name|messageType
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

