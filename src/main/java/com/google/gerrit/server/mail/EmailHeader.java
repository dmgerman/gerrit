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
DECL|package|com.google.gerrit.server.mail
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|EmailHeader
specifier|abstract
class|class
name|EmailHeader
block|{
DECL|method|isEmpty ()
specifier|abstract
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
DECL|method|write (Writer w)
specifier|abstract
name|void
name|write
parameter_list|(
name|Writer
name|w
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|class|String
specifier|static
class|class
name|String
extends|extends
name|EmailHeader
block|{
DECL|field|value
specifier|private
name|java
operator|.
name|lang
operator|.
name|String
name|value
decl_stmt|;
DECL|method|String (java.lang.String v)
name|String
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|write (Writer w)
name|void
name|write
parameter_list|(
name|Writer
name|w
parameter_list|)
throws|throws
name|IOException
block|{
name|w
operator|.
name|write
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Date
specifier|static
class|class
name|Date
extends|extends
name|EmailHeader
block|{
DECL|field|value
specifier|private
name|java
operator|.
name|util
operator|.
name|Date
name|value
decl_stmt|;
DECL|method|Date (java.util.Date v)
name|Date
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Date
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|value
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|write (Writer w)
name|void
name|write
parameter_list|(
name|Writer
name|w
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|SimpleDateFormat
name|fmt
decl_stmt|;
comment|// Mon, 1 Jun 2009 10:49:44 -0700
name|fmt
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, d MMM yyyy HH:mm:ss Z"
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
expr_stmt|;
name|w
operator|.
name|write
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|AddressList
specifier|static
class|class
name|AddressList
extends|extends
name|EmailHeader
block|{
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|Address
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Address
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|AddressList ()
name|AddressList
parameter_list|()
block|{     }
DECL|method|AddressList (Address addr)
name|AddressList
parameter_list|(
name|Address
name|addr
parameter_list|)
block|{
name|add
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
DECL|method|add (Address addr)
name|void
name|add
parameter_list|(
name|Address
name|addr
parameter_list|)
block|{
name|list
operator|.
name|add
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|list
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|write (Writer w)
name|void
name|write
parameter_list|(
name|Writer
name|w
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|len
init|=
literal|8
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
specifier|final
name|Address
name|addr
range|:
name|list
control|)
block|{
name|java
operator|.
name|lang
operator|.
name|String
name|s
init|=
name|addr
operator|.
name|toHeaderString
argument_list|()
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|72
operator|<
name|len
operator|+
name|s
operator|.
name|length
argument_list|()
condition|)
block|{
name|w
operator|.
name|write
argument_list|(
literal|",\r\n\t"
argument_list|)
expr_stmt|;
name|len
operator|=
literal|8
expr_stmt|;
name|first
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|w
operator|.
name|write
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|w
operator|.
name|write
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|len
operator|+=
name|s
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

