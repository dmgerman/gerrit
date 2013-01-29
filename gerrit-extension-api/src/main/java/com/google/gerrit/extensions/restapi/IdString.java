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
DECL|package|com.google.gerrit.extensions.restapi
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
package|;
end_package

begin_comment
comment|/**  * Resource identifier split out from a URL.  *<p>  * Identifiers are URL encoded and usually need to be decoded.  */
end_comment

begin_class
DECL|class|IdString
specifier|public
class|class
name|IdString
block|{
comment|/** Construct an identifier from an already encoded string. */
DECL|method|fromUrl (String id)
specifier|public
specifier|static
name|IdString
name|fromUrl
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|IdString
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|field|urlEncoded
specifier|private
specifier|final
name|String
name|urlEncoded
decl_stmt|;
DECL|method|IdString (String s)
specifier|private
name|IdString
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|urlEncoded
operator|=
name|s
expr_stmt|;
block|}
comment|/** @return the decoded value of the string. */
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|Url
operator|.
name|decode
argument_list|(
name|urlEncoded
argument_list|)
return|;
block|}
comment|/** @return true if the string is the empty string. */
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|urlEncoded
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** @return the original URL encoding supplied by the client. */
DECL|method|encoded ()
specifier|public
name|String
name|encoded
parameter_list|()
block|{
return|return
name|urlEncoded
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|urlEncoded
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|IdString
condition|)
block|{
return|return
name|urlEncoded
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|IdString
operator|)
name|other
operator|)
operator|.
name|urlEncoded
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|other
operator|instanceof
name|String
condition|)
block|{
return|return
name|urlEncoded
operator|.
name|equals
argument_list|(
name|other
argument_list|)
return|;
block|}
return|return
literal|false
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
name|encoded
argument_list|()
return|;
block|}
block|}
end_class

end_unit

