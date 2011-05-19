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
DECL|package|com.google.gerrit.server.auth.ldap
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|auth
operator|.
name|ldap
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|DirContext
import|;
end_import

begin_class
DECL|class|LdapType
specifier|abstract
class|class
name|LdapType
block|{
DECL|field|RFC_2307
specifier|static
specifier|final
name|LdapType
name|RFC_2307
init|=
operator|new
name|Rfc2307
argument_list|()
decl_stmt|;
DECL|method|guessType (final DirContext ctx)
specifier|static
name|LdapType
name|guessType
parameter_list|(
specifier|final
name|DirContext
name|ctx
parameter_list|)
throws|throws
name|NamingException
block|{
specifier|final
name|Attributes
name|rootAtts
init|=
name|ctx
operator|.
name|getAttributes
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|Attribute
name|supported
init|=
name|rootAtts
operator|.
name|get
argument_list|(
literal|"supportedCapabilities"
argument_list|)
decl_stmt|;
if|if
condition|(
name|supported
operator|!=
literal|null
operator|&&
name|supported
operator|.
name|contains
argument_list|(
literal|"1.2.840.113556.1.4.800"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ActiveDirectory
argument_list|(
name|rootAtts
argument_list|)
return|;
block|}
return|return
name|RFC_2307
return|;
block|}
DECL|method|groupPattern ()
specifier|abstract
name|String
name|groupPattern
parameter_list|()
function_decl|;
DECL|method|groupMemberPattern ()
specifier|abstract
name|String
name|groupMemberPattern
parameter_list|()
function_decl|;
DECL|method|accountFullName ()
specifier|abstract
name|String
name|accountFullName
parameter_list|()
function_decl|;
DECL|method|accountEmailAddress ()
specifier|abstract
name|String
name|accountEmailAddress
parameter_list|()
function_decl|;
DECL|method|accountSshUserName ()
specifier|abstract
name|String
name|accountSshUserName
parameter_list|()
function_decl|;
DECL|method|accountMemberField ()
specifier|abstract
name|String
name|accountMemberField
parameter_list|()
function_decl|;
DECL|method|accountPattern ()
specifier|abstract
name|String
name|accountPattern
parameter_list|()
function_decl|;
DECL|class|Rfc2307
specifier|private
specifier|static
class|class
name|Rfc2307
extends|extends
name|LdapType
block|{
annotation|@
name|Override
DECL|method|groupPattern ()
name|String
name|groupPattern
parameter_list|()
block|{
return|return
literal|"(cn=${groupname})"
return|;
block|}
annotation|@
name|Override
DECL|method|groupMemberPattern ()
name|String
name|groupMemberPattern
parameter_list|()
block|{
return|return
literal|"(memberUid=${username})"
return|;
block|}
annotation|@
name|Override
DECL|method|accountFullName ()
name|String
name|accountFullName
parameter_list|()
block|{
return|return
literal|"displayName"
return|;
block|}
annotation|@
name|Override
DECL|method|accountEmailAddress ()
name|String
name|accountEmailAddress
parameter_list|()
block|{
return|return
literal|"mail"
return|;
block|}
annotation|@
name|Override
DECL|method|accountSshUserName ()
name|String
name|accountSshUserName
parameter_list|()
block|{
return|return
literal|"uid"
return|;
block|}
annotation|@
name|Override
DECL|method|accountMemberField ()
name|String
name|accountMemberField
parameter_list|()
block|{
return|return
literal|null
return|;
comment|// Not defined in RFC 2307
block|}
annotation|@
name|Override
DECL|method|accountPattern ()
name|String
name|accountPattern
parameter_list|()
block|{
return|return
literal|"(uid=${username})"
return|;
block|}
block|}
DECL|class|ActiveDirectory
specifier|private
specifier|static
class|class
name|ActiveDirectory
extends|extends
name|LdapType
block|{
DECL|method|ActiveDirectory (final Attributes atts)
name|ActiveDirectory
parameter_list|(
specifier|final
name|Attributes
name|atts
parameter_list|)
throws|throws
name|NamingException
block|{
comment|// Convert "defaultNamingContext: DC=foo,DC=example,DC=com" into
comment|// the a standard DNS name as we would expect to find in the suffix
comment|// part of the userPrincipalName.
comment|//
name|Attribute
name|defaultNamingContext
init|=
name|atts
operator|.
name|get
argument_list|(
literal|"defaultNamingContext"
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultNamingContext
operator|==
literal|null
operator|||
name|defaultNamingContext
operator|.
name|size
argument_list|()
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|NamingException
argument_list|(
literal|"rootDSE has no defaultNamingContext"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|groupPattern ()
name|String
name|groupPattern
parameter_list|()
block|{
return|return
literal|"(&(objectClass=group)(cn=${groupname}))"
return|;
block|}
annotation|@
name|Override
DECL|method|groupMemberPattern ()
name|String
name|groupMemberPattern
parameter_list|()
block|{
return|return
literal|null
return|;
comment|// Active Directory uses memberOf in the account
block|}
annotation|@
name|Override
DECL|method|accountFullName ()
name|String
name|accountFullName
parameter_list|()
block|{
return|return
literal|"${givenName} ${sn}"
return|;
block|}
annotation|@
name|Override
DECL|method|accountEmailAddress ()
name|String
name|accountEmailAddress
parameter_list|()
block|{
return|return
literal|"mail"
return|;
block|}
annotation|@
name|Override
DECL|method|accountSshUserName ()
name|String
name|accountSshUserName
parameter_list|()
block|{
return|return
literal|"${sAMAccountName.toLowerCase}"
return|;
block|}
annotation|@
name|Override
DECL|method|accountMemberField ()
name|String
name|accountMemberField
parameter_list|()
block|{
return|return
literal|"memberOf"
return|;
block|}
annotation|@
name|Override
DECL|method|accountPattern ()
name|String
name|accountPattern
parameter_list|()
block|{
return|return
literal|"(&(objectClass=user)(sAMAccountName=${username}))"
return|;
block|}
block|}
block|}
end_class

end_unit

