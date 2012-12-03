begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
import|import static
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
name|GroupBackends
operator|.
name|GROUP_REF_NAME_COMPARATOR
import|;
end_import

begin_import
import|import static
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
operator|.
name|Helper
operator|.
name|LDAP_UUID
import|;
end_import

begin_import
import|import static
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
operator|.
name|LdapModule
operator|.
name|GROUP_CACHE
import|;
end_import

begin_import
import|import static
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
operator|.
name|LdapModule
operator|.
name|GROUP_EXIST_CACHE
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
name|cache
operator|.
name|LoadingCache
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|data
operator|.
name|GroupReference
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
name|data
operator|.
name|ParameterizedString
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
name|AccountExternalId
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
name|AccountGroup
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
name|CurrentUser
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
name|IdentifiedUser
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
name|GroupBackend
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
name|GroupMembership
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
name|ListGroupMembership
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
name|auth
operator|.
name|ldap
operator|.
name|Helper
operator|.
name|LdapSchema
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InvalidNameException
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
name|ldap
operator|.
name|LdapName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|Rdn
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|LoginException
import|;
end_import

begin_comment
comment|/**  * Implementation of GroupBackend for the LDAP group system.  */
end_comment

begin_class
DECL|class|LdapGroupBackend
specifier|public
class|class
name|LdapGroupBackend
implements|implements
name|GroupBackend
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LdapGroupBackend
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LDAP_NAME
specifier|private
specifier|static
specifier|final
name|String
name|LDAP_NAME
init|=
literal|"ldap/"
decl_stmt|;
DECL|field|GROUPNAME
specifier|private
specifier|static
specifier|final
name|String
name|GROUPNAME
init|=
literal|"groupname"
decl_stmt|;
DECL|field|helper
specifier|private
specifier|final
name|Helper
name|helper
decl_stmt|;
DECL|field|membershipCache
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|membershipCache
decl_stmt|;
DECL|field|existsCache
specifier|private
specifier|final
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|existsCache
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|LdapGroupBackend ( Helper helper, @Named(GROUP_CACHE) LoadingCache<String, Set<AccountGroup.UUID>> membershipCache, @Named(GROUP_EXIST_CACHE) LoadingCache<String, Boolean> existsCache, ProjectCache projectCache, Provider<CurrentUser> userProvider)
name|LdapGroupBackend
parameter_list|(
name|Helper
name|helper
parameter_list|,
annotation|@
name|Named
argument_list|(
name|GROUP_CACHE
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
name|membershipCache
parameter_list|,
annotation|@
name|Named
argument_list|(
name|GROUP_EXIST_CACHE
argument_list|)
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|existsCache
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|)
block|{
name|this
operator|.
name|helper
operator|=
name|helper
expr_stmt|;
name|this
operator|.
name|membershipCache
operator|=
name|membershipCache
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|existsCache
operator|=
name|existsCache
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
block|}
DECL|method|isLdapUUID (AccountGroup.UUID uuid)
specifier|private
name|boolean
name|isLdapUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|uuid
operator|.
name|get
argument_list|()
operator|.
name|startsWith
argument_list|(
name|LDAP_UUID
argument_list|)
return|;
block|}
DECL|method|groupReference (ParameterizedString p, LdapQuery.Result res)
specifier|private
specifier|static
name|GroupReference
name|groupReference
parameter_list|(
name|ParameterizedString
name|p
parameter_list|,
name|LdapQuery
operator|.
name|Result
name|res
parameter_list|)
throws|throws
name|NamingException
block|{
return|return
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|LDAP_UUID
operator|+
name|res
operator|.
name|getDN
argument_list|()
argument_list|)
argument_list|,
name|LDAP_NAME
operator|+
name|LdapRealm
operator|.
name|apply
argument_list|(
name|p
argument_list|,
name|res
argument_list|)
argument_list|)
return|;
block|}
DECL|method|cnFor (String dn)
specifier|private
specifier|static
name|String
name|cnFor
parameter_list|(
name|String
name|dn
parameter_list|)
block|{
try|try
block|{
name|LdapName
name|name
init|=
operator|new
name|LdapName
argument_list|(
name|dn
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|cn
init|=
name|name
operator|.
name|get
argument_list|(
name|name
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|index
init|=
name|cn
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|cn
operator|=
name|cn
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|cn
return|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidNameException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot parse LDAP dn for cn"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|dn
return|;
block|}
annotation|@
name|Override
DECL|method|handles (AccountGroup.UUID uuid)
specifier|public
name|boolean
name|handles
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
return|return
name|isLdapUUID
argument_list|(
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (final AccountGroup.UUID uuid)
specifier|public
name|GroupDescription
operator|.
name|Basic
name|get
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
if|if
condition|(
operator|!
name|handles
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|groupDn
init|=
name|uuid
operator|.
name|get
argument_list|()
operator|.
name|substring
argument_list|(
name|LDAP_UUID
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|CurrentUser
name|user
init|=
name|userProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|user
operator|instanceof
name|IdentifiedUser
operator|)
operator|||
operator|!
name|membershipsOf
argument_list|(
operator|(
name|IdentifiedUser
operator|)
name|user
argument_list|)
operator|.
name|contains
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|existsCache
operator|.
name|get
argument_list|(
name|groupDn
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot lookup group %s in LDAP"
argument_list|,
name|groupDn
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|final
name|String
name|name
init|=
name|LDAP_NAME
operator|+
name|cnFor
argument_list|(
name|groupDn
argument_list|)
decl_stmt|;
return|return
operator|new
name|GroupDescription
operator|.
name|Basic
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|uuid
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|suggest (String name, ProjectControl project)
specifier|public
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|suggest
parameter_list|(
name|String
name|name
parameter_list|,
name|ProjectControl
name|project
parameter_list|)
block|{
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|isLdapUUID
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
name|GroupDescription
operator|.
name|Basic
name|g
init|=
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|GroupReference
operator|.
name|forGroup
argument_list|(
name|g
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|LDAP_NAME
argument_list|)
condition|)
block|{
return|return
name|suggestLdap
argument_list|(
name|name
operator|.
name|substring
argument_list|(
name|LDAP_NAME
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|membershipsOf (IdentifiedUser user)
specifier|public
name|GroupMembership
name|membershipsOf
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|String
name|id
init|=
name|findId
argument_list|(
name|user
operator|.
name|state
argument_list|()
operator|.
name|getExternalIds
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
return|return
name|GroupMembership
operator|.
name|EMPTY
return|;
block|}
try|try
block|{
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
init|=
name|membershipCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
operator|new
name|ListGroupMembership
argument_list|(
name|groups
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getKnownGroups
parameter_list|()
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|g
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|groups
argument_list|)
decl_stmt|;
name|g
operator|.
name|retainAll
argument_list|(
name|projectCache
operator|.
name|guessRelevantGroupUUIDs
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|g
return|;
block|}
block|}
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot lookup membershipsOf %s in LDAP"
argument_list|,
name|id
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|GroupMembership
operator|.
name|EMPTY
return|;
block|}
block|}
DECL|method|findId (final Collection<AccountExternalId> ids)
specifier|private
specifier|static
name|String
name|findId
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
parameter_list|)
block|{
for|for
control|(
specifier|final
name|AccountExternalId
name|i
range|:
name|ids
control|)
block|{
if|if
condition|(
name|i
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_GERRIT
argument_list|)
condition|)
block|{
return|return
name|i
operator|.
name|getSchemeRest
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|suggestLdap (String name)
specifier|private
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|suggestLdap
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|GroupReference
argument_list|>
name|out
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|GROUP_REF_NAME_COMPARATOR
argument_list|)
decl_stmt|;
try|try
block|{
name|DirContext
name|ctx
init|=
name|helper
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
comment|// Do exact lookups until there are at least 3 characters.
name|name
operator|=
name|Rdn
operator|.
name|escapeValue
argument_list|(
name|name
argument_list|)
operator|+
operator|(
operator|(
name|name
operator|.
name|length
argument_list|()
operator|>=
literal|3
operator|)
condition|?
literal|"*"
else|:
literal|""
operator|)
expr_stmt|;
name|LdapSchema
name|schema
init|=
name|helper
operator|.
name|getSchema
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|ParameterizedString
name|filter
init|=
name|ParameterizedString
operator|.
name|asis
argument_list|(
name|schema
operator|.
name|groupPattern
operator|.
name|replace
argument_list|(
name|GROUPNAME
argument_list|,
name|name
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|returnAttrs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|schema
operator|.
name|groupName
operator|.
name|getParameterNames
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|groupBase
range|:
name|schema
operator|.
name|groupBases
control|)
block|{
name|LdapQuery
name|query
init|=
operator|new
name|LdapQuery
argument_list|(
name|groupBase
argument_list|,
name|schema
operator|.
name|groupScope
argument_list|,
name|filter
argument_list|,
name|returnAttrs
argument_list|)
decl_stmt|;
for|for
control|(
name|LdapQuery
operator|.
name|Result
name|res
range|:
name|query
operator|.
name|query
argument_list|(
name|ctx
argument_list|,
name|params
argument_list|)
control|)
block|{
name|out
operator|.
name|add
argument_list|(
name|groupReference
argument_list|(
name|schema
operator|.
name|groupName
argument_list|,
name|res
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot close LDAP query handle"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot query LDAP for groups matching requested name"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot query LDAP for groups matching requested name"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
block|}
end_class

end_unit

