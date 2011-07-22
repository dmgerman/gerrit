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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|ParameterizedString
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

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
name|PartialResultException
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
name|BasicAttribute
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

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchControls
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
name|SearchResult
import|;
end_import

begin_comment
comment|/** Supports issuing parameterized queries against an LDAP data source. */
end_comment

begin_class
DECL|class|LdapQuery
class|class
name|LdapQuery
block|{
DECL|field|ALL_ATTRIBUTES
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ALL_ATTRIBUTES
init|=
literal|null
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|String
name|base
decl_stmt|;
DECL|field|searchScope
specifier|private
specifier|final
name|SearchScope
name|searchScope
decl_stmt|;
DECL|field|pattern
specifier|private
specifier|final
name|ParameterizedString
name|pattern
decl_stmt|;
DECL|field|returnAttributes
specifier|private
specifier|final
name|String
index|[]
name|returnAttributes
decl_stmt|;
DECL|method|LdapQuery (final String base, final SearchScope searchScope, final ParameterizedString pattern, final Set<String> returnAttributes)
name|LdapQuery
parameter_list|(
specifier|final
name|String
name|base
parameter_list|,
specifier|final
name|SearchScope
name|searchScope
parameter_list|,
specifier|final
name|ParameterizedString
name|pattern
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|returnAttributes
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
name|this
operator|.
name|searchScope
operator|=
name|searchScope
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
if|if
condition|(
name|returnAttributes
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|returnAttributes
operator|=
operator|new
name|String
index|[
name|returnAttributes
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|returnAttributes
operator|.
name|toArray
argument_list|(
name|this
operator|.
name|returnAttributes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|returnAttributes
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getParameters ()
name|List
argument_list|<
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|pattern
operator|.
name|getParameterNames
argument_list|()
return|;
block|}
DECL|method|query (final DirContext ctx, final Map<String, String> params)
name|List
argument_list|<
name|Result
argument_list|>
name|query
parameter_list|(
specifier|final
name|DirContext
name|ctx
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|NamingException
block|{
specifier|final
name|SearchControls
name|sc
init|=
operator|new
name|SearchControls
argument_list|()
decl_stmt|;
specifier|final
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|res
decl_stmt|;
name|sc
operator|.
name|setSearchScope
argument_list|(
name|searchScope
operator|.
name|scope
argument_list|()
argument_list|)
expr_stmt|;
name|sc
operator|.
name|setReturningAttributes
argument_list|(
name|returnAttributes
argument_list|)
expr_stmt|;
name|res
operator|=
name|ctx
operator|.
name|search
argument_list|(
name|base
argument_list|,
name|pattern
operator|.
name|getRawPattern
argument_list|()
argument_list|,
name|pattern
operator|.
name|bind
argument_list|(
name|params
argument_list|)
argument_list|,
name|sc
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|List
argument_list|<
name|Result
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|Result
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
while|while
condition|(
name|res
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|Result
argument_list|(
name|res
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PartialResultException
name|e
parameter_list|)
block|{       }
return|return
name|r
return|;
block|}
finally|finally
block|{
name|res
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|Result
class|class
name|Result
block|{
DECL|field|atts
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
name|atts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Attribute
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|Result (final SearchResult sr)
name|Result
parameter_list|(
specifier|final
name|SearchResult
name|sr
parameter_list|)
block|{
if|if
condition|(
name|returnAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|String
name|attName
range|:
name|returnAttributes
control|)
block|{
specifier|final
name|Attribute
name|a
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|attName
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|atts
operator|.
name|put
argument_list|(
name|attName
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|NamingEnumeration
argument_list|<
name|?
extends|extends
name|Attribute
argument_list|>
name|e
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
operator|.
name|getAll
argument_list|()
decl_stmt|;
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|Attribute
name|a
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|atts
operator|.
name|put
argument_list|(
name|a
operator|.
name|getID
argument_list|()
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
name|atts
operator|.
name|put
argument_list|(
literal|"dn"
argument_list|,
operator|new
name|BasicAttribute
argument_list|(
literal|"dn"
argument_list|,
name|sr
operator|.
name|getNameInNamespace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getDN ()
name|String
name|getDN
parameter_list|()
throws|throws
name|NamingException
block|{
return|return
name|get
argument_list|(
literal|"dn"
argument_list|)
return|;
block|}
DECL|method|get (final String attName)
name|String
name|get
parameter_list|(
specifier|final
name|String
name|attName
parameter_list|)
throws|throws
name|NamingException
block|{
specifier|final
name|Attribute
name|att
init|=
name|getAll
argument_list|(
name|attName
argument_list|)
decl_stmt|;
return|return
name|att
operator|!=
literal|null
operator|&&
literal|0
operator|<
name|att
operator|.
name|size
argument_list|()
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|att
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getAll (final String attName)
name|Attribute
name|getAll
parameter_list|(
specifier|final
name|String
name|attName
parameter_list|)
block|{
return|return
name|atts
operator|.
name|get
argument_list|(
name|attName
argument_list|)
return|;
block|}
DECL|method|attributes ()
name|Set
argument_list|<
name|String
argument_list|>
name|attributes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|atts
operator|.
name|keySet
argument_list|()
argument_list|)
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
try|try
block|{
return|return
name|getDN
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

