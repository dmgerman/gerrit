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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|ImmutableList
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
name|common
operator|.
name|Nullable
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
name|mail
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_comment
comment|/**  * A simplified representation of an RFC 2045-2047 mime email message used for representing received  * emails inside Gerrit. It is populated by the MailParser after MailReceiver has received a  * message. Transformations done by the parser include stitching mime parts together, transforming  * all content to UTF-16 and removing attachments.  *  *<p>A valid {@link MailMessage} contains at least the following fields: id, from, to, subject and  * dateReceived.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|MailMessage
specifier|public
specifier|abstract
class|class
name|MailMessage
block|{
comment|// Unique Identifier
DECL|method|id ()
specifier|public
specifier|abstract
name|String
name|id
parameter_list|()
function_decl|;
comment|// Envelop Information
DECL|method|from ()
specifier|public
specifier|abstract
name|Address
name|from
parameter_list|()
function_decl|;
DECL|method|to ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|Address
argument_list|>
name|to
parameter_list|()
function_decl|;
DECL|method|cc ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|Address
argument_list|>
name|cc
parameter_list|()
function_decl|;
comment|// Metadata
DECL|method|dateReceived ()
specifier|public
specifier|abstract
name|DateTime
name|dateReceived
parameter_list|()
function_decl|;
DECL|method|additionalHeaders ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|additionalHeaders
parameter_list|()
function_decl|;
comment|// Content
DECL|method|subject ()
specifier|public
specifier|abstract
name|String
name|subject
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|textContent ()
specifier|public
specifier|abstract
name|String
name|textContent
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|htmlContent ()
specifier|public
specifier|abstract
name|String
name|htmlContent
parameter_list|()
function_decl|;
comment|// Raw content as received over the wire
annotation|@
name|Nullable
DECL|method|rawContent ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|rawContent
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|rawContentUTF ()
specifier|public
specifier|abstract
name|String
name|rawContentUTF
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_MailMessage
operator|.
name|Builder
argument_list|()
return|;
block|}
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|id (String val)
specifier|public
specifier|abstract
name|Builder
name|id
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|from (Address val)
specifier|public
specifier|abstract
name|Builder
name|from
parameter_list|(
name|Address
name|val
parameter_list|)
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Address
argument_list|>
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|addTo (Address val)
specifier|public
name|Builder
name|addTo
parameter_list|(
name|Address
name|val
parameter_list|)
block|{
name|toBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|ccBuilder ()
specifier|public
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Address
argument_list|>
name|ccBuilder
parameter_list|()
function_decl|;
DECL|method|addCc (Address val)
specifier|public
name|Builder
name|addCc
parameter_list|(
name|Address
name|val
parameter_list|)
block|{
name|ccBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|dateReceived (DateTime val)
specifier|public
specifier|abstract
name|Builder
name|dateReceived
parameter_list|(
name|DateTime
name|val
parameter_list|)
function_decl|;
DECL|method|additionalHeadersBuilder ()
specifier|public
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|additionalHeadersBuilder
parameter_list|()
function_decl|;
DECL|method|addAdditionalHeader (String val)
specifier|public
name|Builder
name|addAdditionalHeader
parameter_list|(
name|String
name|val
parameter_list|)
block|{
name|additionalHeadersBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|subject (String val)
specifier|public
specifier|abstract
name|Builder
name|subject
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|textContent (String val)
specifier|public
specifier|abstract
name|Builder
name|textContent
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|htmlContent (String val)
specifier|public
specifier|abstract
name|Builder
name|htmlContent
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|rawContent (ImmutableList<Integer> val)
specifier|public
specifier|abstract
name|Builder
name|rawContent
parameter_list|(
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|val
parameter_list|)
function_decl|;
DECL|method|rawContentUTF (String val)
specifier|public
specifier|abstract
name|Builder
name|rawContentUTF
parameter_list|(
name|String
name|val
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|MailMessage
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

